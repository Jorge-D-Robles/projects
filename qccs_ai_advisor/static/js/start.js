document.addEventListener('DOMContentLoaded', () => {
    const modalOpen = document.querySelector('.modal-open'); // Assuming you have a trigger element with this class
    const modalCloseButtons = document.querySelectorAll('.modal-close'); // Selects all close buttons, including the "X"
    const modal = document.getElementById('modal'); // The modal itself
    const modalOverlay = document.getElementById('modalOverlay'); // The overlay

    // Function to open the modal
    const openModal = () => {
      modal.classList.remove('hidden');
      modalOverlay.classList.remove('hidden');
    };

    // Function to close the modal
    const closeModal = () => {
      modal.classList.add('hidden');
      modalOverlay.classList.add('hidden');
    };

    // Open modal event (if you have a button or another element to open the modal)
    if (modalOpen) {
      modalOpen.addEventListener('click', openModal);
    }

    // Close modal when clicking on the overlay
    modalOverlay.addEventListener('click', (event) => {
      if (event.target === modalOverlay) {
        closeModal();
      }
    });

    // Close modal when clicking the "X" or any .modal-close element
    modalCloseButtons.forEach(closeButton => {
      closeButton.addEventListener('click', closeModal);
    });

    // Close modal with the ESC key
    document.addEventListener('keydown', (event) => {
      if (event.key === 'Escape' && !modal.classList.contains('hidden')) {
        closeModal();
      }
    });
  });
