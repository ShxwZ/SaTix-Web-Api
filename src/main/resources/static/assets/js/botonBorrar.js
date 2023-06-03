	function deleteModalLink(link) {
	const elementId = link.getAttribute('data-id');
	const modal = document.querySelector('#delete-modal');
	const linkEl = modal.querySelector('.modal-footer a');
	const linkHref = linkEl.getAttribute('href');
	linkEl.setAttribute('href', linkHref + elementId);
}
