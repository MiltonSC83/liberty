function eliminar(id){
	swal({
	title: "Esta seguro de eliminar?",
	text: "Una vez eliminado no se podra recuperar el registro",
	icon: "warning",
	buttons: true,
	dangerMode: true,
	})
	.then((OK) => {
	if (OK) {
		$.ajax({
			url:"/eliminar/"+ id,
			success: function(res) {
				Console.log(res);
			}
		});
	  swal("Registro Eliminado Correctamente", {
	  icon: "success",
	}).then(()=>{
		if(OK){
			location.href="/listar";
		}
	});
	}else {
	   swal("Cancelado con Exito");
	}
	});
}