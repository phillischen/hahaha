package clientUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;




/**
 * 绘图区域的事件监听器类继承了MouseAdapter类，该类已经实现了MouseListener和MouseMotionListener接口
 * 
 */
public class DrawListener extends MouseAdapter {

	private Graphics2D graphics2d;// 绘图区域的Graphic2D对象，用于绘制给定的图形
	private ToolsPanel toolsPanel;// 图形选择工具条
	private int x1, x2, y1, y2, startx, starty;
	private boolean flag;// 用于标记当前绘制的任意多边形是否绘制完成

	/**
	 * 构造方法
	 * 
	 * @param graphics2d 画图的画笔对象
	 * @param toolsPanel 工具栏对象
	 * @param colorPanel 颜色栏对象
	 */
	public DrawListener(Graphics2D graphics2d, ToolsPanel toolsPanel) {
		this.graphics2d = graphics2d;
		this.toolsPanel = toolsPanel;
		this.flag = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		String action = toolsPanel.getTool();
		if (action.equals("2")) {// 当用户选择文字输入工具执行的动作
			x2 = e.getX();
			y2 = e.getY();
			//注意：如果用户最终没有输入，而是点了cancel，返回值为null 
			String a= toolsPanel.inputText();
			System.out.print(a);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		String action = toolsPanel.getTool();
		if (action.equals("811")) {// 当用户选择画刷工具执行的动作
			// 设置画笔的粗细
			graphics2d.setStroke(new BasicStroke(7));
			// 获取拖动过程中的坐标值
			x2 = e.getX();
			y2 = e.getY();
			// 绘制直线
			graphics2d.drawLine(x1, y1, x2, y2);
			// 交换坐标
			x1 = x2;
			y1 = y2;
		} else if (action.equals("1")) {// 当用户选择的是铅笔工具执行的动作
			// 获取拖动过程中的坐标值
			x2 = e.getX();
			y2 = e.getY();
			// 绘制直线
			graphics2d.drawLine(x1, y1, x2, y2);
			// 交换坐标
			x1 = x2;
			y1 = y2;
		} else if (action.equals("7")) {// 当用户选择的是橡皮工具执行的动作
			graphics2d.setStroke(new BasicStroke(8));
			// 获取拖动过程中的坐标值
			x2 = e.getX();
			y2 = e.getY();
			// 绘制直线
			graphics2d.drawLine(x1, y1, x2, y2);
			// 交换坐标
			x1 = x2;
			y1 = y2;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		String action = toolsPanel.getTool();
		if (!action.equals("14111")) {// 判断当前绘制的不是任意多边形
			x1 = e.getX();
			y1 = e.getY();
		} else {// 如果当前绘制是任意多边形
			if (!flag) {// 判断当前的鼠标按下的位置是多边形的起点
				startx = x1 = e.getX();
				starty = y1 = e.getY();
				flag = true;
			}
		}
		if (e.getButton() == 1) {
			// 如果按下的是鼠标的左键那么将graphics2d颜色设置为选择的颜色
			graphics2d.setColor(toolsPanel.getColorL());
			//如果是橡皮擦，变成白色
			if (toolsPanel.getTool().equals("7")) {
				graphics2d.setColor(Color.WHITE);
			}
			System.out.println("left");
		} else if (e.getButton() == 3) {// 如果按下的是鼠标的右键那么将graphics2d颜色设置为选择的颜色
			graphics2d.setColor(toolsPanel.getColorR());
			System.out.println("right");
		}
		graphics2d.setStroke(new BasicStroke(1));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		String action = toolsPanel.getTool();
		if (action.equals("3")) {// 当用户选择的是直线执行绘制直线的步骤
			x2 = e.getX();
			y2 = e.getY();
			graphics2d.drawLine(x1, y1, x2, y2);
		} else if (action.equals("2xxxx")) {// 当用户选择的是选框工具按钮执行的动作
			x2 = e.getX();
			y2 = e.getY();
			//drawDash();
		} else if (action.equals("4")) {// 当用户选择的是矩形工具按钮的动作
			x2 = e.getX();
			y2 = e.getY();
			graphics2d.drawRect(x1, y1, x2 - x1, y2 - y1);
		} else if (action.equals("xxx")) {// 当用户选择的是圆角矩形工具执行的动作
			x2 = e.getX();
			y2 = e.getY();
			graphics2d.drawRoundRect(x1, y1, x2 - x1, y2 - y1, 12, 12);
		} else if (action.equals("5")||action.equals("6")) {// 当用户选择的是圆形工具执行的动作
			x2 = e.getX();
			y2 = e.getY();
			graphics2d.drawOval(x1, y1, x2 - x1, y2 - y1);
		} else if (action.equals("14xxxx")) {// 当用户学选择的是任意多边形工具执行的动作
			x2 = e.getX();
			y2 = e.getY();
			if ((x2 - startx) * (x2 - startx) + (y2 - starty) * (y2 - starty) <= 64) {// 判断当前的点是不是多边形的终点
				x2 = startx;
				y2 = starty;
				flag = false;
			}
			graphics2d.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		} else if (action.equals("9xxxx")) {// 当用户选择的是喷枪工具执行的动作
			// 实例化一个随机数生成的对象
			x1 = e.getX();
			y1 = e.getY();
			Random random = new Random();
			for (int i = 0; i < 200; i++) {
				x2 = random.nextInt(25) - 12;
				y2 = random.nextInt(25) - 12;
				if (x2 * x2 + y2 * y2 > 121)
					continue;// 如果生成的点不在以点击的位置为圆心以11为半径的圆内那么直接跳过这个点
				graphics2d.drawLine(x1 + x2, y1 + y2, x1 + x2, y1 + y2);
			}
		}
	}


}
