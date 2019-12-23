package cn.saatana.module.system.file.controller;

import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.module.system.file.entity.Resource;
import cn.saatana.module.system.file.repository.ResourceRepository;
import cn.saatana.module.system.file.service.ResourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resource")
public class ResourceController extends CurdController<ResourceService, ResourceRepository, Resource> {
	@Value("${spring.servlet.multipart.max-file-size}")
	private DataSize fileMaxSize;

	public String getBaseDir() {
		return appProp.getFileBaseDir();
	}

	public String getTempDir() {
		return appProp.getFileTempDir() + currentToken() + "/";
	}

	public void clearTemp(String fileName) {
		File dir = new File(getTempDir());
		File file = new File(getTempDir() + fileName);
		if (dir.exists() && dir.isDirectory()) {
			if (file.renameTo(new File(getBaseDir() + fileName))) {
				dir.deleteOnExit();
			} else {
				throw new RuntimeException("清理临时文件失败。");
			}
		}
	}

	@PostMapping("upload")
	public Res<String> upload(MultipartFile file) {
		if (DataSize.ofBytes(file.getSize()).compareTo(fileMaxSize) > 0) {
			throw new RuntimeException("文件大小超出限制。");
		}
		String[] temp = file.getOriginalFilename().split("\\.");
		if (temp.length < 2) {
			throw new RuntimeException("文件名格式不正确。");
		}
		String name = UUID.randomUUID().toString().replaceAll("-", "") + "." + temp[temp.length - 1];
		String path = getTempDir() + name;
		File target = new File(path);
		target.getParentFile().mkdirs();
		try {
			file.transferTo(target);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("保存文件失败。");
		}
		return Res.ok(name);
	}

	@RequestMapping("download/{id}")
	public void downlaod(@PathVariable String id, HttpServletResponse response) throws UnsupportedEncodingException {
		Resource entity = service.get(id);
		String[] temp = entity.getPath().split("\\.");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-Disposition", "attachment;fileName="
				+ new String(entity.getLabel().getBytes("gbk"), "iso8859-1") + "." + temp[temp.length - 1]);
		String path = appProp.getFileBaseDir() + entity.getPath();
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))) {
			byte[] buffer = new byte[1024];
			ServletOutputStream os = response.getOutputStream();
			while (bis.read(buffer) > 0) {
				os.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("文件读取失败。");
		}
	}

	@Override
	public Res<Resource> create(@RequestBody @Validated Resource entity, BindingResult result)
			throws UnsupportedEncodingException {
		clearTemp(entity.getPath());
		return super.create(entity, result);
	}

	@Override
	public Res<Resource> update(@RequestBody @Validated Resource entity, BindingResult result) {
		clearTemp(entity.getPath());
		return super.update(entity, result);
	}

	@Override
	public Res<Resource> delete(String id) {
		Resource res = service.get(id);
		String path = getBaseDir() + res.getPath();
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		return super.delete(id);
	}

	@Override
	public Res<List<Resource>> deleteAll(@RequestBody List<String> ids) {
		ids.forEach(id -> {
			Resource res = service.get(id);
			String path = getBaseDir() + res.getPath();
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		});
		return super.deleteAll(ids);
	}

}
