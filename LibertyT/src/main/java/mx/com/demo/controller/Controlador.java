package mx.com.demo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.demo.interfaceService.IpersonaService;
import mx.com.demo.interfaces.IPersona;
import mx.com.demo.modelo.Persona;
import mx.com.demo.service.PersonaService;
import mx.com.demo.utils.RenderizadorPaginas;

@Controller
@RequestMapping
public class Controlador {
	
	@Autowired
	private IpersonaService service;
	
	@Autowired
	private IPersona service1;
	
	@GetMapping("/listar")
	public String listar(@RequestParam(name="page", defaultValue = "0")int page, Model model) {
		//Paginacion
		Pageable personaPageable = PageRequest.of(page, 10);
		List<Persona>personas=service.listar();
		Page<Persona> persona = service1.findAll(personaPageable);
		RenderizadorPaginas<Persona> renderizadorPaginas = new RenderizadorPaginas<Persona>("/listar", persona);
		
		model.addAttribute("page", renderizadorPaginas);
		model.addAttribute("personas", persona);
		return "index";
	}
	
	@GetMapping("/new")
	public String agregar(Model model) {
		model.addAttribute("persona", new Persona());
		return "form";
	}
	
	@PostMapping("/save")
	public String save(@RequestParam(name= "file", required=false) MultipartFile foto, @Validated Persona p, Model model, RedirectAttributes flash) {
		
		if(!foto.isEmpty()) {
			String ruta = "C://Temporal//uploads";
			String nombreUnico = UUID.randomUUID().toString()+"-"+foto.getOriginalFilename();
			
			try {
				byte[] bytes = foto.getBytes();
				Path rutaAbsoluta = Paths.get(ruta + "//" + nombreUnico);
				Files.write(rutaAbsoluta, bytes);
				p.setFoto(nombreUnico);
				if(!this.validarExtension(foto)) {
					model.addAttribute("fail","La extension no es valida");
					return "form";
				}
			}catch(Exception e) {
				
			}
			flash.addFlashAttribute("success","Foto subida");
			System.out.println("Nombre unico de la foto "+ nombreUnico);
		}
		else {
			p.setFoto("usuario.png");
		}
		service.save(p);
		return "redirect:/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable int id,Model model) {
		Optional<Persona>persona=service.listarId(id);
		model.addAttribute("persona", persona);
		return "formedit";
	}
	
	@GetMapping("/eliminar/{id}")
	public String delete(Model model, @PathVariable int id) {
		service.delete(id);
		return "redirect:/listar";
	}
	
	//Validar que archivo que se sube a foto sea una imagen
	public boolean validarExtension(MultipartFile archivo) {
		try {
			ImageIO.read(archivo.getInputStream()).toString();
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
		
	}

}
