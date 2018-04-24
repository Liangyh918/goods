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
		 * commons-fileupload���ϴ�����
		 */
		//��������
		FileItemFactory factory = new DiskFileItemFactory();
		/*
		 * ��������������
		 */
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80*1024);//���õ����ϴ��ļ�������Ϊ80kb
		/*
		 * 3.����request�õ�List<FileItem>
		 */
		
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(req);
		} catch (Exception e) {
			// TODO: handle exception
			error("�ϴ����ļ�������80kb",req,res);
			return;
		}
		
		/*
		 * 4.��List<FileItem>��װ��Book������
		 * 4.1 ���Ȱѡ���ͨ���ֶΡ��ŵ�Map�У��ٰ�Mapװ����Book
		 * ��Category�����ڽ������ߵĹ�ϵ
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		for (FileItem fileItem:fileItemList) {
			if(fileItem.isFormField()){//�������ͨ���ֶ�
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}			
		}
		Book book = CommonUtils.toBean(map, Book.class);//��Map�д󲿷����ݷ�װ��Book������
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		/*
		 * 4.2���ϴ���ͼƬ��������
		 *   >��ȡ�ļ�������ȡ֮
		 *   >���ļ����ǰ׺��ʹ��uuidǰ׺�������ļ�ͬ������
		 *   >У���ļ�����չ����ֻ����jpg
		 *   >У��ͼƬ�ĳߴ�
		 *   >ָ��ͼƬ�ı���·��������Ҫʹ��ServletContext#getRealPath()
		 *   >����֮
		 *   >��ͼƬ·�����ø�Book����
		 */
		//��ȡ�ļ���
		FileItem fileItem = fileItemList.get(1);
		String filename = fileItem.getName();
		//��ȡ�ļ�������Ϊ����������ϴ��ľ���·��
		int index = filename.lastIndexOf("\\");
		if(index != -1){
			filename = filename.substring(index+1);
		}
		
		//���ļ������uuidǰ׺�������ļ�ͬ��
		filename = CommonUtils.uuid()+"_"+filename;
		//У���ļ����Ƶ���չ��
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("�ϴ���ͼƬ��չ��������jpg", req, res);
			return;
			}
		
		
		//У��ͼƬ�ĳߴ�
		//�����ϴ���ͼƬ����ͼƬnew��ͼƬ����
		//image��Icon��ImageIcon��BufferedImage��ImageIO
		/*
		 * ����ͼƬ��
		 * 1.��ȡ����·��
		 */
		String savepath = this.getServletContext().getRealPath("/book_img");
		/*
		 * 2.����Ŀ���ļ�
		 */
		File destFile = new File(savepath,filename);
		/*
		 * 3.�����ļ�
		 */
		try {
			fileItem.write(destFile);//�������ʱ�ļ��ض���ָ����·������ɾ����ʱ�ļ�
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//У��ߴ�
		//1.ʹ���ļ�·������ImageIcon
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		//2.ͬ��ImageIcon�õ�Image����
		Image image =  icon.getImage();
		//3.��ȡ���������У��
		if(image.getWidth(null)>350||image.getHeight(null)>350){
			error("���ϴ���ͼƬ�ߴ糬��350*350",req,res);
			destFile.delete();
			return;
		}
		
		//��ͼƬ��·�����ø�Book����
		book.setImage_w("book_img/" + filename);
		
		
		//�ϴ�Сͼ
		//��ȡ�ļ���
				 fileItem = fileItemList.get(2);
				 filename = fileItem.getName();
				//��ȡ�ļ�������Ϊ����������ϴ��ľ���·��
				 index = filename.lastIndexOf("\\");
				if(index != -1){
					filename = filename.substring(index+1);
				}
				
				//���ļ������uuidǰ׺�������ļ�ͬ��
				filename = CommonUtils.uuid()+"_"+filename;
				//У���ļ����Ƶ���չ��
				if(!filename.toLowerCase().endsWith(".jpg")){
					error("�ϴ���ͼƬ��չ��������jpg", req, res);
					return;
					}
				
				
				//У��ͼƬ�ĳߴ�
				//�����ϴ���ͼƬ����ͼƬnew��ͼƬ����
				//image��Icon��ImageIcon��BufferedImage��ImageIO
				/*
				 * ����ͼƬ��
				 * 1.��ȡ����·��
				 */
				savepath = this.getServletContext().getRealPath("/book_img");
				/*
				 * 2.����Ŀ���ļ�
				 */
				destFile = new File(savepath,filename);
				/*
				 * 3.�����ļ�
				 */
				try {
					fileItem.write(destFile);//�������ʱ�ļ��ض���ָ����·������ɾ����ʱ�ļ�
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				//У��ߴ�
				//1.ʹ���ļ�·������ImageIcon
				icon = new ImageIcon(destFile.getAbsolutePath());
				//2.ͬ��ImageIcon�õ�Image����
				image =  icon.getImage();
				//3.��ȡ���������У��
				if(image.getWidth(null)>350||image.getHeight(null)>350){
					error("���ϴ���ͼƬ�ߴ糬��350*350",req,res);
					destFile.delete();
					return;
				}
				
				//��ͼƬ��·�����ø�Book����
				book.setImage_b("book_img/" + filename);
				
				//����service��ɱ���
				book.setBid(CommonUtils.uuid());
				bookService.add(book);
				
				//����ɹ���Ϣת����msg.jsp
				req.setAttribute("msg", "���ͼ��ɹ���");
				req.getRequestDispatcher("/adminjsps/msg.jsp").forward(req, res);
	}
		
	
	private void error(String msg,HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		req.setAttribute("msg", msg);
		req.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(req, res);
	}

}
