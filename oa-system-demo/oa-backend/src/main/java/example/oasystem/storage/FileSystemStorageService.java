package example.oasystem.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import example.oasystem.entries.FileItem;
import example.oasystem.entries.FilePermission;
import example.oasystem.repository.FilePermissionRepository;
import example.oasystem.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
//	@Autowired
	private final FilePermissionRepository filePermissionRepository;
	private final FileRepository fileRepository;

//	public FileSystemStorageService(FilePermissionRepository filePermissionRepository) {
//		this.filePermissionRepository = filePermissionRepository;
//	}

	@Autowired
	public FileSystemStorageService(StorageProperties properties,
									FilePermissionRepository filePermissionRepository,
									FileRepository fileRepository) {


        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }

		this.rootLocation = Paths.get(properties.getLocation());
		this.filePermissionRepository = filePermissionRepository;
		this.fileRepository = fileRepository;

	}

	@Override
	public void store(MultipartFile file,String path,Integer employeeId) {
		if (path.equals("rembursement")){
			Path destinationFile = this.rootLocation
					.toAbsolutePath().normalize()
					.resolve(path)
					.resolve(file.getOriginalFilename())
					.normalize();
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
						StandardCopyOption.REPLACE_EXISTING);
				return;
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		FileItem fileItem = fileRepository.findByName(getRootPath(path));
		FilePermission filePermission = filePermissionRepository
				.findByFileIdAndEmployeeId(fileItem.getId(),employeeId);
		if (!filePermission.getWrite()){
			throw new StorageException("Permission denied");
//			return;
		}
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
//			System.out.println(this.rootLocation.toAbsolutePath().normalize());
//			System.out.println(this.rootLocation.toFile().getAbsolutePath());
//			System.out.println(this.rootLocation.toAbsolutePath().normalize().resolve(Paths.get("kd")));
//			System.out.println(this.rootLocation.toAbsolutePath().normalize().resolve(Paths.get(path)));
//			System.out.println(this.rootLocation.resolve(Paths.get("kkkk/hhh")).normalize().toAbsolutePath());
//			System.out.println(this.rootLocation.resolve(Paths.get(normalizedPath)).resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath());
			Path destinationFile = this.rootLocation
					.toAbsolutePath().normalize()
					.resolve(normalizedPath)
					.resolve(file.getOriginalFilename())
					.normalize();
//			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
//				// This is a security check
//				System.out.println(destinationFile.getParent());
//				System.out.println(this.rootLocation.toAbsolutePath());
//				throw new StorageException(
//						"Cannot store file outside current directory.");
//			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	public static String getRootPath(String path) {
		if (path == null || path.isEmpty() || path.equals("/")) {
			return "";
		}
		String[] parts = path.split("/");
		return parts.length > 1 ? parts[1] : "";
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	public Path load(String filename, String path) {
		String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
		Path destinationFile = this.rootLocation
				.toAbsolutePath().normalize()
				.resolve(normalizedPath)
				.resolve(filename)
				.normalize();
		return destinationFile;
	}

	@Override
	public Resource loadAsResource(String filename,String path) {
		try {
			Path file = load(filename,path);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
