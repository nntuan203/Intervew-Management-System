$(document).ready(function () {

    let multipleCancelButton = new Choices('.choices-multiple-remove-button', {
    removeItemButton: true,
    // maxItemCount:5,
    // searchResultLimit: 5,
    // renderChoiceLimit: 5
});
});

const dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
const dropdownList = dropdownElementList.map((dropdownToggleEl) => {
    return new mdb.Dropdown(dropdownToggleEl);
});