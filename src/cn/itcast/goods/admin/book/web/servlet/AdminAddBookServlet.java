package cn.itcast.goods.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;

public class AdminAddBookServlet extends HttpServlet {
	BookService bookService = new BookService();
	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws
	ServletException,IOException{
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		/*
		 * commons-fileupload的上传三步
		 */
		//创建工具
		FileItemFactory factory = new DiskFileItemFactory();
		/*
		 * 创建解析器对象
		 */
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80*1024);//设置单个上传文件的上限为80kb
		/*
		 * 3.解析request得到List<FileItem>
		 */
		
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(req);
		} catch (Exception e) {
			// TODO: handle exception
			error("上传的文件超出了80kb",req,res);
			return;
		}
		
		/*
		 * 4.把List<FileItem>封装到Book对象中
		 * 4.1 首先把“普通表单字段”放到Map中，再把Map装还成Book
		 * 和Category对象，在建立两者的关系
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		for (FileItem fileItem:fileItemList) {
			if(fileItem.isFormField()){//如果是普通表字段
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}			
		}
		Book book = CommonUtils.toBean(map, Book.class);//把Map中大部分数据封装到Book对象中
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		/*
		 * 4.2把上传的图片保存起来
		 *   >获取文件名：截取之
		 *   >给文件添加前缀：使用uuid前缀，避免文件同名现像
		 *   >校验文件的扩展名：只能是jpg
		 *   >校验图片的尺寸
		 *   >指定图片的保存路劲，这需要使用ServletContext#getRealPath()
		 *   >保存之
		 *   >把图片路径设置给Book对象
		 */
		//获取文件名
		FileItem fileItem = fileItemList.get(1);
		String filename = fileItem.getName();
		//截取文件名，因为部分浏览器上传的绝对路径
		int index = filename.lastIndexOf("\\");
		if(index != -1){
			filename = filename.substring(index+1);
		}
		
		//给文件名添加uuid前缀，避免文件同名
		filename = CommonUtils.uuid()+"_"+filename;
		//校验文件名称的扩展名
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("上传的图片扩展名必须是jpg", req, res);
			return;
			}
		
		
		//校验图片的尺寸
		//保存上传的图片，把图片new成图片对象：
		//image，Icon，ImageIcon、BufferedImage、ImageIO
		/*
		 * 保存图片：
		 * 1.获取真是路径
		 */
		String savepath = this.getServletContext().getRealPath("/book_img");
		/*
		 * 2.创建目标文件
		 */
		File destFile = new File(savepath,filename);
		/*
		 * 3.保存文件
		 */
		try {
			fileItem.write(destFile);//他会把临时文件重定向到指定的路径，在删除临时文件
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//校验尺寸
		//1.使用文件路径创建ImageIcon
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		//2.同过ImageIcon得到Image对象
		Image image =  icon.getImage();
		//3.获取宽高来进行校验
		if(image.getWidth(null)>350||image.getHeight(null)>350){
			error("您上传的图片尺寸超出350*350",req,res);
			destFile.delete();
			return;
		}
		
		//把图片的路径设置给Book对象
		book.setImage_w("book_img/" + filename);
		
		
		//上传小图
		//获取文件名
				 fileItem = fileItemList.get(2);
				 filename = fileItem.getName();
				//截取文件名，因为部分浏览器上传的绝对路径
				 index = filename.lastIndexOf("\\");
				if(index != -1){
					filename = filename.substring(index+1);
				}
				
				//给文件名添加uuid前缀，避免文件同名
				filename = CommonUtils.uuid()+"_"+filename;
				//校验文件名称的扩展名
				if(!filename.toLowerCase().endsWith(".jpg")){
					error("上传的图片扩展名必须是jpg", req, res);
					return;
					}
				
				
				//校验图片的尺寸
				//保存上传的图片，把图片new成图片对象：
				//image，Icon，ImageIcon、BufferedImage、ImageIO
				/*
				 * 保存图片：
				 * 1.获取真是路径
				 */
				savepath = this.getServletContext().getRealPath("/book_img");
				/*
				 * 2.创建目标文件
				 */
				destFile = new File(savepath,filename);
				/*
				 * 3.保存文件
				 */
				try {
					fileItem.write(destFile);//他会把临时文件重定向到指定的路径，在删除临时文件
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				//校验尺寸
				//1.使用文件路径创建ImageIcon
				icon = new ImageIcon(destFile.getAbsolutePath());
				//2.同过ImageIcon得到Image对象
				image =  icon.getImage();
				//3.获取宽高来进行校验
				if(image.getWidth(null)>350||image.getHeight(null)>350){
					error("您上传的图片尺寸超出350*350",req,res);
					destFile.delete();
					return;
				}
				
				//把图片的路径设置给Book对象
				book.setImage_b("book_img/" + filename);
				
				//调用service完成保存
				book.setBid(CommonUtils.uuid());
				bookService.add(book);
				
				//保存成功信息转发到msg.jsp
				req.setAttribute("msg", "添加图书成功！");
				req.getRequestDispatcher("/adminjsps/msg.jsp").forward(req, res);
	}
		
	
	private void error(String msg,HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		req.setAttribute("msg", msg);
		req.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(req, res);
	}

}
