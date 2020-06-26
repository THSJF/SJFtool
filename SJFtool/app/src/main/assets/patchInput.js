javascript:
function dispatchInput(dom, st) {
	var evt = new InputEvent('input',
		{
			inputType: 'insertText',
			data: st,
			dataTransfer: null,
			isComposing: false
		});
	dom.value = st;
	dom.dispatchEvent(evt);
}
dispatchInput(document.getElementById('login-username'),"%d");
dispatchInput(document.getElementById('login-passwd'),"%s");

