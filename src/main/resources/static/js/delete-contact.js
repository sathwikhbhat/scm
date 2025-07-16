console.log("Hello from Delete Contact.js");

// Select modal element
const deleteModalElement = document.getElementById('delete-contact-modal');

// Flowbite modal options
const deleteModalOptions = {
    placement: 'center',
    backdrop: 'dynamic',
    backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => console.log('modal is hidden'),
    onShow: () => console.log('modal is shown'),
    onToggle: () => console.log('modal has been toggled')
};

const deleteContactConfig = {
    id: 'delete-contact-modal',
    override: true
};

const deleteModal = new Modal(deleteModalElement, deleteModalOptions, deleteContactConfig);

let id = null;

// Show modal
function showWarningModal(ID) {
    id = ID;
    deleteModal.show();
}

// Hide modal
function hideWarningModal() {
    deleteModal.hide();
}

// Load data into modal dynamically
function deleteContact() {
    fetch(`/api/contacts/delete/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete contact');
            }

            console.log('Contact deleted successfully');

            hideWarningModal();
            location.reload();
        })
        .catch(error => {
            console.error('Error deleting contact:', error);
            hideWarningModal();
            alert('Could not delete contact. Please try again later.');
        });
}