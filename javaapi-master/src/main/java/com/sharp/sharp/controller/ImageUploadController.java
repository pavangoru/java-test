package com.sharp.sharp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sharp.sharp.awss3.AmazonImage;
import com.sharp.sharp.awss3.AmazonS3ImageService;
import com.sharp.sharp.entity.ImagesEntity;
import com.sharp.sharp.service.ImageUploadService;
import com.sharp.sharp.serviceImpl.AWSS3ServiceImpl;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@RestController
@RequestMapping("/images")
public class ImageUploadController {
	@Autowired
	private ImageUploadService ImageService;
	@Autowired
	private AWSS3ServiceImpl aws;
	@Value("${s3.endpointUrl}")
	private String url;
	@Autowired
	private AmazonS3ImageService s3imageService;

	/*
	 * @GetMapping("/demo/get/{id}") public ResponseEntity<ByteArrayResource>
	 * getFile(@PathVariable String id) throws IOException { final ByteArrayResource
	 * byteArrayResource = new ByteArrayResource(this.aws.getFile(id)); return
	 * ResponseEntity.ok().contentLength(byteArrayResource.contentLength())
	 * .contentType(MediaType.parseMediaType("application/octet-stream"))
	 * .header("Content-Disposition", "attachment; filename=" +
	 * id).body(byteArrayResource); }
	 */

	@GetMapping("/getAllIMages/")
	public HashMap<String, Object> getAllImages() {
		HashMap<String, Object> resultMap = new HashMap<>();
		List<ImagesEntity> IMageList = ImageService.getAllImagesById();
		if (!Sharp6Validation.isEmpty(IMageList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", IMageList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "No Images are available.");
		}
		return resultMap;
	}

	@PostMapping("/uploadFile/")
	public HashMap<String, Object> uploadFile(@RequestPart("file") MultipartFile file,

			@RequestParam("fieName") String fieName, @RequestParam("categoryid") int categoryid,

			@RequestParam("categoryname") String categoryname, @RequestParam("defaultImage") boolean defaultImage)
			throws IOException {

		ImagesEntity entity = new ImagesEntity();
		HashMap<String, Object> map = new HashMap<String, Object>();

		entity.setId(String.valueOf(System.currentTimeMillis()));

		entity.setFileName(fieName);
		entity.setCategoryid(categoryid);
		entity.setCategoryname(categoryname);
		entity.setDefaultImage(defaultImage);

		// entity.setData(file.getBytes());
		entity.setFileType(file.getContentType());
		entity.setSize(file.getSize());

		String imageurl = null;
		ImagesEntity dbFile;
		try {

			// String imageurl = aws.uploadFile(file);
			AmazonImage list = s3imageService.insertImages(file, fieName);
			/*
			 * Map<String, Object> result = aws.fileUploadMultipartFile("123456", file);
			 * Set<Entry<String, Object>> entrySet = result.entrySet(); for (Entry<String,
			 * Object> entry : entrySet) { if (entry.getKey().contains("uploadFile")) {
			 * imageurl = entry.getValue().toString(); } }
			 */
			imageurl = list.getImageUrl();
	
			System.out.println("image url" + imageurl);
			entity.setFileDownloadUri(imageurl);

			dbFile = ImageService.upoadImages(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put(Constants.STATUS, false);
			map.put(Constants.ERROR_MSG, e.getMessage());
			map.put("errorVaue", e.getLocalizedMessage());
			return map;

		}

		/*
		 * String fileDownloadUri =
		 * ServletUriComponentsBuilder.fromCurrentContextPath().path("imageurl")
		 * .path(dbFile.getId()).toUriString();
		 */

		/*
		 * return new ImagesEntity(dbFile.getUserId(), file.getName(),
		 * file.getContentType(), file.getSize(), fileDownloadUri);
		 */
		map.put(Constants.STATUS, true);
		map.put("imagesData", dbFile);
		map.put("url", imageurl);
		return map;
	}

	@GetMapping("/getImageById/{id}")
	public HashMap<String, Object> getImage(@PathVariable("id") long id) {
		HashMap<String, Object> resultMap = new HashMap<>();
		ImagesEntity imageObj = ImageService.getImage(id);
		if (!Sharp6Validation.isEmpty(imageObj)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("image", imageObj);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "No Images Are available.");
		}
		return resultMap;
	}

	@PostMapping("/deleteImageById/")
	public HashMap<String, Object> deleteImageById(@RequestParam("Id") long Id,
			@RequestParam("categoryId") int categoryId) {
		HashMap<String, Object> resultMap = new HashMap<>();
		String imageObj = ImageService.deleteImageById(Id, categoryId);
		if (imageObj.equals(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "Image deleted successfully");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Image  Not deleted.");
		}
		return resultMap;
	}

	@PostMapping("/imageNameUpdate/")
	public HashMap<String, Object> updateImageName(@RequestParam("shortName") String shortName,
			@RequestParam("Id") long Id) {
		HashMap<String, Object> resultMap = new HashMap<>();
		ImagesEntity imageObj = ImageService.updateImageName(shortName, Id);
		if (!Sharp6Validation.isEmpty(imageObj)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", imageObj);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Image Name Not Updated.");
		}
		return resultMap;
	}

	@GetMapping("/download/{keyname}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String keyname) {
		ByteArrayOutputStream downloadInputStream = aws.downloadFile(keyname);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + keyname + "\"")
				.body(downloadInputStream.toByteArray());
	}

}
