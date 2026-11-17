package example.oasystem.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collectors;

import example.oasystem.entries.Employee;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.storage.StorageFileNotFoundException;
import example.oasystem.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/file-upload")
public class FileUploadController {

	private final StorageService storageService;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public FileUploadController(StorageService storageService,
								EmployeeRepository employeeRepository) {
		this.storageService = storageService;
		this.employeeRepository = employeeRepository;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename,@RequestParam("path") String path){

		Resource file = storageService.loadAsResource(filename,path);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
	}

	@PostMapping()
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path,
												   RedirectAttributes redirectAttributes,
												   Principal principal) {

		Employee employee = employeeRepository.findByUsername(principal.getName());
		storageService.store(file,path,employee.getId());
//		redirectAttributes.addFlashAttribute("message",
//				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return ResponseEntity.ok(file.getOriginalFilename());
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
