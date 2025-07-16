console.log("Hello from contacts.js");

// Select modal element
const $targetEl = document.getElementById('contact-modal');

// Flowbite modal options
const options = {
    placement: 'center',
    backdrop: 'dynamic',
    backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => console.log('modal is hidden'),
    onShow: () => console.log('modal is shown'),
    onToggle: () => console.log('modal has been toggled')
};

const instanceOptions = {
    id: 'contact-modal',
    override: true
};

const modal = new Modal($targetEl, options, instanceOptions);

// Show modal
function showContactModal() {
    modal.show();
}

// Hide modal
function hideContactModal() {
    modal.hide();
}

// Load data into modal dynamically
function loadContactData(id) {
    showContactModal();

    fetch(`/api/contacts/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch contact');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('contact-name').textContent = data.name || 'N/A';
            document.getElementById('contact-email').textContent = data.email || 'N/A';
            document.getElementById('contact-phone').textContent = data.phoneNumber || 'N/A';
            document.getElementById('contact-address').textContent = data.address || 'N/A';
            document.getElementById('contact-description').textContent = data.description || 'No description';

            const favElement = document.getElementById('contact-favourite');
            const star = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512">
                            <!--!Font Awesome Free 6.7.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.-->
                            <path fill="#FFD43B" d="M316.9 18C311.6 7 300.4 0 288.1 0s-23.4 7-28.8 18L195 150.3 51.4 171.5c-12 1.8-22 10.2-25.7 21.7s-.7 24.2 7.9 32.7L137.8 329 113.2 474.7c-2 12 3 24.2 12.9 31.3s23 8 33.8 2.3l128.3-68.5 128.3 68.5c10.8 5.7 23.9 4.9 33.8-2.3s14.9-19.3 12.9-31.3L438.5 329 542.7 225.9c8.6-8.5 11.7-21.2 7.9-32.7s-13.7-19.9-25.7-21.7L381.2 150.3 316.9 18z"/></svg>`;
            favElement.innerHTML = data.favourite ? `<span class="inline-block w-6 h-6" title="Marked as favourite">${star}</span>` : '';

            const websiteLink = document.getElementById('contact-website');
            websiteLink.href = data.websiteLink || '#';
            websiteLink.textContent = data.websiteLink || 'No website';

            const linkedinLink = document.getElementById('contact-linkedin');
            linkedinLink.href = data.linkedinLink || '#';
            linkedinLink.textContent = data.linkedinLink || 'No LinkedIn';

            const imageElement = document.getElementById('contact-image');
            if (data.pictureUrl) {
                imageElement.src = data.pictureUrl;
                imageElement.alt = `${data.name}'s Profile Picture`;
            } else {
                imageElement.src = '/img/default-profile-pic.png';
                imageElement.alt = 'Default Profile Picture';
            }
        })
        .catch(error => {
            console.error('Error loading contact:', error);
            hideContactModal();
            alert('Could not load contact details. Please try again later.');
        });
}