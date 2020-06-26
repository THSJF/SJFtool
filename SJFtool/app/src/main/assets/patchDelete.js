javascript:
function patchDeleteClass(cls){
	var v=document.querySelectorAll('.'+cls)[0];
	v.parentNode.removeChild(v);
}
patchDeleteClass('login-left');
patchDeleteClass('top-banner');
patchDeleteClass('title-line');
patchDeleteClass('m-header');
patchDeleteClass('footer');
patchDeleteClass('line');

