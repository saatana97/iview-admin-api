package cn.saatana.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.internal.util.ReflectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.alibaba.druid.util.StringUtils;

import cn.saatana.core.annotation.ExcelField;
import cn.saatana.core.dict.entity.Dictionary;
import cn.saatana.core.dict.service.DictionaryService;

/**
 * Excel表格导入导出工具类
 *
 * @author 向文可
 *
 */
@Component
public class ExcelUtils {
	private static DictionaryService dictService;

	@Autowired
	public void setDictionaryService(DictionaryService dictService) {
		ExcelUtils.dictService = dictService;
	}

	/**
	 * 导入数据
	 *
	 * @param type   实体类类型
	 * @param file   数据文件
	 * @param groups 字段分组
	 * @return
	 */
	public static <T> List<T> importExcel(Class<T> type, InputStream is, int[] groups) {
		List<T> data = new ArrayList<>();
		try {
			Workbook wb = new HSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(0);
			List<ExcelField> annos = new ArrayList<>();
			// 扫描有注解的方法
			List<Method> methods = new ArrayList<>();
			Arrays.asList(ReflectionUtils.getUniqueDeclaredMethods(type)).forEach(item -> {
				ExcelField anno = item.getAnnotation(ExcelField.class);
				if (anno != null && containsAny(groups, anno.groups())) {
					Method method = item;
					// 如果是Getter方法就尝试获取对应的Setter方法
					if (method.getName().startsWith("get")) {
						Class<?> superType = null;
						String setterName = "set" + method.getName().substring(3);
						do {
							try {
								method = type.getDeclaredMethod(setterName, method.getReturnType());
							} catch (NoSuchMethodException | SecurityException e) {
								method = null;
								System.err.println(type.getSimpleName() + " -> " + setterName);
							}
							superType = type.getSuperclass();
						} while (!(superType instanceof Object));
					}
					annos.add(anno);
					methods.add(method);
				}
			});
			// 扫描有注解的字段的Setter方法
			getFields(type).forEach(field -> {
				ExcelField anno = field.getAnnotation(ExcelField.class);
				// 判断是否是指定分组的字段的Setter方法
				boolean res = anno != null;
				if (res) {
					Method method = ReflectHelper.findSetterMethod(type, field.getName(), field.getType());
					if (method != null) {
						annos.add(anno);
						methods.add(method);
					}
				}
			});
			// 排序
			Map<Integer, Integer> sortMap = new HashMap<>();
			annos.forEach(anno -> {
				sortMap.put(anno.hashCode(), annos.indexOf(anno));
			});
			Collections.sort(annos, (prev, next) -> {
				return prev.sort() - next.sort();
			});
			// 去掉标题和列名
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				T record = type.newInstance();
				for (int j = 0; j < row.getLastCellNum(); j++) {
					ExcelField anno = annos.get(j);
					Method method = methods.get(sortMap.get(anno.hashCode()));
					if (method != null) {
						try {
							Cell cell = row.getCell(j);
							String cellValue = cell.getStringCellValue();
							Object setValue = null;
							if (method.getParameters()[0].getType().getName().equals(Date.class.getName())) {
								// 日期处理
								setValue = new SimpleDateFormat(anno.dateFormat()).format(cellValue);
							} else if (!StringUtils.isEmpty(anno.dictType())) {
								// 字典处理
								Dictionary dict = new Dictionary();
								dict.setCode(anno.dictType());
								dict.setLabel(cellValue);
								List<Dictionary> dicts = dictService.findList(dict, StringMatcher.EXACT);
								if (dicts.size() > 0) {
									setValue = dicts.get(0).getValue();
								}
							} else {
								// 默认处理
								setValue = cellValue;
							}
							method.setAccessible(true);
							method.invoke(record, setValue);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				data.add(record);
			}
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 导出数据
	 *
	 * @param type     实体类类型
	 * @param data     数据
	 * @param hasIndex 是否添加序号
	 * @param title    标题
	 * @param groups   字段分组
	 * @param os       输出流
	 */
	public static <T> void exportExcel(Class<T> type, List<T> data, boolean hasIndex, String title, int[] groups,
			OutputStream os) {
		if (data != null && data.size() > 0) {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			List<ExcelField> annos = new ArrayList<>();
			// 扫描有注解的方法
			List<Method> methods = new ArrayList<>();
			Arrays.asList(ReflectionUtils.getUniqueDeclaredMethods(type)).forEach(method -> {
				ExcelField anno = method.getAnnotation(ExcelField.class);
				// 判断是否是无参、有返回值、指定分组的字段
				boolean res = anno != null && method.getParameterCount() == 0
						&& !method.getReturnType().getName().equals(Void.class.getName())
						&& containsAny(groups, anno.groups());
				if (res) {
					annos.add(anno);
					methods.add(method);
				}
			});
			// 扫描有注解的字段的Getter方法
			getFields(type).forEach(field -> {
				ExcelField anno = field.getAnnotation(ExcelField.class);
				// 判断是否是指定分组的字段的Getter方法
				boolean res = anno != null && containsAny(groups, anno.groups());
				if (res) {
					Method method = ReflectHelper.findGetterMethodForFieldAccess(field, field.getName());
					if (method != null) {
						annos.add(anno);
						methods.add(method);
					}
				}
			});
			Map<Integer, Integer> sortMap = new HashMap<>();
			annos.forEach(anno -> {
				sortMap.put(anno.hashCode(), annos.indexOf(anno));
			});
			Collections.sort(annos, (prev, next) -> {
				return prev.sort() - next.sort();
			});
			// 写入标题
			if (!StringUtils.isEmpty(title)) {
				int colsCount = annos.size() - 1;
				if (hasIndex) {
					colsCount++;
				}
				Cell cell = null;
				if (colsCount >= 2) {
					cell = addRangeCell(sheet, 0, 0, 0, colsCount);
				} else {
					Row row = addRow(sheet);
					row.setHeightInPoints(30f);
					cell = addCell(sheet, row.getRowNum(), 0);
				}
				cell.setCellValue(title);
				cell.setCellStyle(getHanderStyle(wb));
			}
			// 写入列名
			Row titleRow = addRow(sheet);
			titleRow.setHeightInPoints(20);
			int titleColIndex = 0;
			if (hasIndex) {
				Cell cell = addCell(sheet, sheet.getLastRowNum(), titleColIndex++);
				cell.setCellValue("序号");
				cell.setCellStyle(getTitleStyle(wb));
			}
			for (ExcelField anno : annos) {
				Cell cell = addCell(sheet, sheet.getLastRowNum(), titleColIndex++);
				cell.setCellValue(anno.title());
				cell.setCellStyle(getTitleStyle(wb));
			}
			// 写入数据
			for (int i = 0; i < data.size(); i++) {
				T record = data.get(i);
				addRow(sheet);
				int recordColIndex = 0;
				if (hasIndex) {
					Cell cell = addCell(sheet, sheet.getLastRowNum(), recordColIndex++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(i + 1);
					cell.setCellStyle(getDataStyle(wb));
				}
				for (int j = 0; j < annos.size(); j++) {
					Cell cell = addCell(sheet, sheet.getLastRowNum(), recordColIndex++);
					cell.setCellStyle(getDataStyle(wb));
					ExcelField anno = annos.get(j);
					Method method = methods.get(sortMap.get(anno.hashCode()));
					try {
						Object returnValue = method.invoke(record);
						String cellValue = "";
						cell.setCellType(CellType.STRING);
						if (returnValue != null) {
							if (returnValue instanceof Date) {
								// 日期处理
								cellValue = new SimpleDateFormat(anno.dateFormat()).format(returnValue);
								cell.setCellValue(cellValue);
							} else if (!StringUtils.isEmpty(anno.dictType()) && returnValue instanceof Integer) {
								// 字典处理
								Dictionary dict = new Dictionary();
								dict.setCode(anno.dictType());
								dict.setValue((Integer) returnValue);
								List<Dictionary> dicts = dictService.findList(dict, StringMatcher.EXACT);
								if (dicts.size() > 0) {
									cellValue = dicts.get(0).getLabel();
								}
								cell.setCellValue(cellValue);
							} else {
								// 默认处理
								cellValue = returnValue.toString();
								cell.setCellValue(cellValue);
							}
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
			// 自动调整宽度
			int colsCount = sheet.getRow(sheet.getLastRowNum()).getLastCellNum() + 1;
			for (int i = 0; i < colsCount; i++) {
				sheet.autoSizeColumn(i);
			}
			try {
				wb.write(os);
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean containsAny(int[] need, int[] has) {
		boolean res = true;
		if (need != null && has != null) {
			for (int i = 0; i < has.length; i++) {
				boolean contain = res = Arrays.asList(need).contains(has[i]);
				if (contain) {
					break;
				}
			}
		}
		return res;
	}

	private static <T> List<Field> getFields(Class<T> type) {
		List<Field> res = Arrays.asList(type.getDeclaredFields());
		Class<?> superType = type.getSuperclass();
		if (!(superType instanceof Object)) {
			res.addAll(getFields(superType));
		}
		return res;
	}

	private static Row addRow(Sheet sheet) {
		return sheet.createRow(sheet.getLastRowNum() + 1);
	}

	private static Cell addCell(Sheet sheet, int row, int col) {
		return sheet.getRow(row).createCell(col);
	}

	private static Cell addRangeCell(Sheet sheet, int rowStart, int rowEnd, int colStart, int colEnd) {
		sheet.addMergedRegion(new CellRangeAddress(rowStart, rowEnd, colStart, colEnd));
		return sheet.createRow(rowStart).createCell(colStart);
	}

	private static CellStyle getHanderStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 16);
		font.setFontName("黑体");
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		return style;
	}

	private static CellStyle getTitleStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("黑体");
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		return style;
	}

	private static CellStyle getDataStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		return style;
	}
}
