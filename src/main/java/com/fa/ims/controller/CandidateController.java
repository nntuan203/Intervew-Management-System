package com.fa.ims.controller;


import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.CandidateDetailDto;
import com.fa.ims.dto.CandidateDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.repository.HighestLevelRepository;
import com.fa.ims.repository.PositionRepository;
import com.fa.ims.repository.SkillsRepository;
import com.fa.ims.service.CandidateService;
import com.fa.ims.service.FileService;
import com.fa.ims.service.UserService;
import com.fa.ims.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.fa.ims.constant.CommonConstants.Candidate_Status_Create;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private FileService fileService;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private HighestLevelRepository highestLevelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateService candidateService;


    @GetMapping("/list")
    public String findPaginated(Model model, @RequestParam(required = false, defaultValue = "1") int pageNo,
                                @RequestParam(required = false, defaultValue = "") String keyword,
                                @RequestParam(required = false, defaultValue = "") String status) {

        System.out.println(keyword);
        Page<Candidate> page = candidateService.findCandidateByKeyWord(pageNo, CommonConstants.PAGE_SIZE,keyword.trim(), status);
        model.addAttribute("candidateList", page);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", CommonConstants.PAGE_SIZE);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("statusList",candidateService.findAllStatus());
        model.addAttribute("keyword",keyword);
        model.addAttribute("status",status);
        return "candidate/list-candidate";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'MANAGER')")
    @GetMapping("/create")
    public String createCandidate(Model model) {
        model.addAttribute("candidateDto", new CandidateDto());
        model.addAttribute("statusList", Candidate_Status_Create);
        this.getAttribute(model);
        return "candidate/create-candidate";
    }


    @PostMapping("/create")
    public String saveCandidate(@ModelAttribute(name = "candidateDto") @Valid CandidateDto candidateDto,
                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Boolean isEmailExist = candidateService.isEmailExists(candidateDto.getCandidateEmail());
        Boolean isMoreThan6 = candidateService.isSkillMoreThan6(candidateDto);
        if(bindingResult.hasErrors() || isEmailExist || isMoreThan6) {
            this.getAttribute(model);
            model.addAttribute("candidateDto",candidateDto);
            model.addAttribute("statusList", Candidate_Status_Create);
            model.addAttribute("isMoreThan6", isMoreThan6);
            model.addAttribute("isEmailExist", isEmailExist);
            model.addAttribute("messFail", Message.MESSAGE_005);
            return "candidate/create-candidate";
        }
        if(candidateService.saveNewCandidate(candidateDto)) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_006);
            return "redirect:/candidate/create";

        }
        redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_005);
        return "redirect:/candidate/create";
    }

    @GetMapping("/detail/{id}")
    public String detailCandidate(@PathVariable("id") Long id, Model model) {
        CandidateDetailDto candidateDetailDto = candidateService.findCandidateDetail(id);
        model.addAttribute("candidateDetail", candidateDetailDto);
        model.addAttribute("id", id);
        return "candidate/view-candidate";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'MANAGER')")
    @GetMapping("/update/{id}")
    public String updateCandidate(@PathVariable("id") Long id, Model model) {
        CandidateDto candidateDto = candidateService.findCandidateDtoById(id);
        Boolean isReadOnly = candidateService.isReadOnly(candidateDto.getCandidateStatus());
        this.getAttribute(model);
        model.addAttribute("candidateDtoUpdate", candidateDto);
        model.addAttribute("isReadOnly", isReadOnly);
        if(!isReadOnly) {
            model.addAttribute("statusList", Candidate_Status_Create);
        }
        return "candidate/update-candidate";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("candidateDtoUpdate") @Valid CandidateDto candidateDtoUpdate,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Boolean isEmailExist = candidateService.isEmailExist(candidateDtoUpdate.getCandidateEmail(), id);
        Boolean isReadOnly = candidateService.isReadOnly(candidateDtoUpdate.getCandidateStatus());
        if (isEmailExist || bindingResult.hasErrors()) {
            this.getAttribute(model);
            model.addAttribute("isEmailExist", isEmailExist);
            if(!isReadOnly) {
                model.addAttribute("statusList", Candidate_Status_Create);
            }
            model.addAttribute("isReadOnly", isReadOnly);
            model.addAttribute("messFail", Message.MESSAGE_007);
            return ("candidate/update-candidate");
        }
        if((candidateService.saveUpdateCandidate(candidateDtoUpdate, id) != null)) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_008);
            return "redirect:/candidate/detail/" + id;
        }
        redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_007);
        return "redirect:/candidate/detail/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'MANAGER')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if(candidateService.delete(id)) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_009);
            return "redirect:/candidate/list";
        }
        redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_010);
        return "redirect:/candidate/list";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'MANAGER')")
    @ResponseBody
    @GetMapping("/file/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest httpRequest)
            throws IOException {
        String path = httpRequest.getRequestURL()
                .toString().split("file/")[1];
        Resource resource = fileService.loadFile(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    public void getAttribute(Model model) {
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("skillList",skillsRepository.findAll() );
        model.addAttribute("levelList", highestLevelRepository.findAll());
        model.addAttribute("recruiterList", userService.findUserByRole("ROLE_RECRUITER"));
    }

}
