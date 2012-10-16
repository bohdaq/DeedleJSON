$(function(){
		
	//contact form
	$('#contact_form').submit(function(){
		var form = this,
			fields = [
				{
					name: 'name',
					message: 'Write Your Name',
					def: 'Name',
					//gr: 'e'
				},{
					name: 'email',
					message: 'Write Your E-Mail',
					def: 'E-Mail',
					//gr: 'e'
				}
			];
		
		res = check_form(this, fields);
		
		if (res) {
			//send form
			$.post('mailer.php', $(this).serializeArray(), function(data){
				if (data == 'ok'){
					alert('Message sent!');
					form.reset();
				}else{
					alert(data);
				}
			});
			return false;
		};
		
		return res;
	});
});

function check_form(self, fields){
	var res = true;
	
	var scroll = true;
	for(ind in fields){
		var field = fields[ind];
		var f = $('*[name="'+field.name+'"]', self);
		switch (f.attr('type')){
			case 'checkbox':
				if (f.filter(':checked').length === 0) {
					f.last().tipsy({
						gravity: 'nw',
						fallback: field.message,
						trigger: 'manual'
					}).tipsy('show');
					if (scroll) {
						f.last().focus();
						scroll = false;
					};
					res = false;
				}else{
					f.last().tipsy('hide');
				}
				break;
				
			default:
				if ($.trim(f.val()) === '' || $.trim(f.val()) == field.def) {
					f.tipsy({
						gravity: field.gr || 'w',
						fallback: field.message,
						trigger: 'manual'
					}).tipsy('show');
					if (scroll) {
						f.last().focus();
						scroll = false;
					};
					res = false;
				}else{
					f.tipsy('hide');
				}
				break;
		}
	}

	return res;
}