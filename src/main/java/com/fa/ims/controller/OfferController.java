package com.fa.ims.controller;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.ExportDateDto;
import com.fa.ims.dto.OfferDto;
import com.fa.ims.dto.OfferRecordDto;
import com.fa.ims.entity.Offer;
import com.fa.ims.enums.ContractType;
import com.fa.ims.repository.*;
import com.fa.ims.service.ExcelService;
import com.fa.ims.service.OfferService;
import com.fa.ims.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobLevelRepository jobLevelRepository;

    @Autowired
    private ExcelService excelService;
    @Autowired
    private CandidateStatusRepository candidateStatusRepository;

    @InitBinder("valueSearch")
    public void initBinderSearch(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    @InitBinder({"depart", "status"})
    public void initBinderOptional(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String viewOfferList(Model model,
                                @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(required = false, defaultValue = "") String valueSearch,
                                @RequestParam(required = false) String depart,
                                @RequestParam(required = false) String status) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //List offer
        Page<OfferRecordDto> offerList = null;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            offerList = offerService.getAllOfferRecordDto(valueSearch, depart, status, pageNumber);
        }

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            //List offer by manager name is approve
            offerList = offerService.getAllOfferRecordDtoByManagerName(valueSearch, depart, status, pageNumber, authentication.getName());
            //Count all offer status is waiting for approval
            model.addAttribute("offerApproveSize", offerService.countAllOfferApproveByManagerName(authentication.getName()));
        }

        //Paging
        model.addAttribute("offerList", offerList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", CommonConstants.PAGE_SIZE);
        model.addAttribute("totalPages", offerList != null ? offerList.getTotalPages() : 0);
        model.addAttribute("totalElements", offerList != null ? offerList.getTotalElements() : 0);
        //List to selectBox
        model.addAttribute("departList", offerService.getAllDepartment());
        //Search param return to view
        model.addAttribute("valueSearch", valueSearch);
        model.addAttribute("status", status);
        model.addAttribute("depart", depart);
        //Date to export
        model.addAttribute("exportDateDto", new ExportDateDto());


        return "offer/list-offer";
    }

    @GetMapping("/create")
    public String createOfferPage(Model model) {
        model.addAttribute("offerDto", new OfferDto());
        loadAllAttributesOfferForm(model);
        return "offer/create-offer";
    }

    @PostMapping("/create")
    public String createOffer(@ModelAttribute("offerDto") @Valid OfferDto offerDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (!bindingResult.hasErrors()) {
            Offer offer = offerService.saveOffer(offerService.mapOfferDtoToOffer(offerDto));
            if (offer != null) {
                //Update Candidate Status is waiting for offer approval.
                offerService.updateCandidateStatusIdByOfferId(offer.getOfferId(),
                        candidateStatusRepository.findCandidateStatusByStatusName("Waiting for approval").getStatusId());

                redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_025);
            } else {
                redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_024);
            }
            return "redirect:/offer";
        }
        loadAllAttributesOfferForm(model);
        return "offer/create-offer";
    }

    @PreAuthorize("hasAnyRole('ROLE_RECRUITER', 'ROLE_ADMIN')")
    @GetMapping("/update/{offerId}")
    public String updateOffer(@PathVariable("offerId") Long offerId, Model model) {

        Offer offer = offerService.findById(offerId).orElseThrow();

        model.addAttribute("offerDto", offerService.mapOfferToOfferDto(offer));

        loadAllAttributesOfferForm(model);

        switch (offerService.getCandidateStatusesNameByOfferId(offerId)) {
            case "Waiting for approval":
            case "Rejected offer":
                return "offer/update-offer";
            case "Accepted offer":
            case "Declined offer":
            case "Cancelled offer":
                return "offer/details-offer";
            default:
                return "offer/update-offer-approved";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_RECRUITER', 'ROLE_ADMIN')")
    @PostMapping("/update/{offerId}")
    public String updateOffer(@PathVariable("offerId") Long offerId,
                              @ModelAttribute("offerDto") @Valid OfferDto offerDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (!bindingResult.hasErrors()) {
            Offer offer = offerService.mapOfferDtoToOffer(offerDto);
            offer.setOfferId(offerId);

            Long statusId = candidateStatusRepository.findCandidateStatusByStatusName(offerDto.getStatus()).getStatusId();
            offerService.updateCandidateStatusIdByOfferId(offerId, statusId);

            if (offerService.saveOffer(offer) != null) {
                redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_030);
            } else {
                redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_029);
            }
            return "redirect:/offer";
        }
        loadAllAttributesOfferForm(model);

        Offer offer = offerService.findById(offerId).orElseThrow();
        offerDto.setCreatedDate(offer.getCreatedDate().toLocalDate());
        offerDto.setLastModifiedBy(offer.getLastModifiedBy());
        offerDto.setLastModifiedDate(offer.getLastModifiedDate().toLocalDate());

        return "offer/update-offer";
    }

    @GetMapping("/{offerId}")
    public String detailsOffer(@PathVariable("offerId") Long offerId,Model model){

        Offer offer = offerService.findById(offerId).orElseThrow();

        model.addAttribute("offerDto", offerService.mapOfferToOfferDto(offer));

        loadAllAttributesOfferForm(model);
        return "offer/details-offer";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @GetMapping("/approve/{offerId}")
    public String approveOffer(@PathVariable("offerId") Long offerId, Model model) {

        Offer offer = offerService.findById(offerId).orElseThrow();

        model.addAttribute("offerDto", offerService.mapOfferToOfferDto(offer));

        loadAllAttributesOfferForm(model);
        return "offer/approve-offer";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @PostMapping("/approve/{offerId}")
    public String approveOffer(@PathVariable("offerId") Long offerId,
                               @ModelAttribute("offerDto") @Valid OfferDto offerDto,
                               RedirectAttributes redirectAttributes) {
        Long statusId = candidateStatusRepository.findCandidateStatusByStatusName(offerDto.getStatus()).getStatusId();

        if (offerService.updateCandidateStatusIdByOfferId(offerId, statusId) == 1) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_030);
        } else {
            redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_029);
        }

        return "redirect:/offer";
    }

    @PostMapping("/export")
    public void exportToExcel(@ModelAttribute("exportDateDto") @Valid ExportDateDto exportDateDto,
                              BindingResult bindingResult,
                              HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!bindingResult.hasErrors()) {

            List<OfferRecordDto> offerList = null;

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))
                    || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                offerList = offerService.getAllOfferDto(exportDateDto.getDateFrom(), exportDateDto.getDateTo());
            }

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
                offerList = offerService.getAllOfferDtoByManagerName(authentication.getName(), exportDateDto.getDateFrom(), exportDateDto.getDateTo());
            }

            response.setContentType("application/vnd.ms-excel");
            String fileName = String.format("OfferList-%s-To-%s.xlsx", exportDateDto.getDateFrom().toString(), exportDateDto.getDateTo().toString());

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            excelService.exportOffersToExcel(offerList, response.getOutputStream());
        }
    }

    private void loadAllAttributesOfferForm(Model model) {
        model.addAttribute("interviewMap", offerService.getInterviewsMap());
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("departList", departRepository.findAll());
        model.addAttribute("managerList", userRepository.findAllManager());
        model.addAttribute("recruiterList", userRepository.findAllRecruiter());
        model.addAttribute("levelList", jobLevelRepository.findAll());
        model.addAttribute("enumContractType", ContractType.values());
    }
}
