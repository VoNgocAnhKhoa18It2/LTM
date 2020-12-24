package vnakhoa.vku.Graphic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D.Double;

import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.text.Document;

public class TextFieldRounded extends JTextField {
	
	
	public TextFieldRounded() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TextFieldRounded(Document arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}
	public TextFieldRounded(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	public TextFieldRounded(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	public TextFieldRounded(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	@Override 
	 protected void paintComponent(Graphics g) {
		    if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
		      Graphics2D g2 = (Graphics2D) g.create();
		      g2.setPaint(getBackground());
		      g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
		          0, 0, getWidth() - 1, getHeight() - 1));
		      g2.dispose();
		    }
		    super.paintComponent(g);
		  }
		  @Override public void updateUI() {
		    super.updateUI();
		    setOpaque(false);
		    setBorder(new RoundedCornerBorder());
		  }
		};
		class RoundedCornerBorder extends AbstractBorder {
		  private static final Color ALPHA_ZERO = new Color(0x0, true);
		  @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		    Graphics2D g2 = (Graphics2D) g.create();
		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    Shape border = getBorderShape(x, y, width - 1, height - 1);
		    g2.setPaint(ALPHA_ZERO);
		    Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
		    corner.subtract(new Area(border));
		    g2.fill(corner);
		    g2.setPaint(Color.GRAY);
		    g2.draw(border);
		    g2.dispose();
		  }
		  public Double getBorderShape(int x, int y, int w, int h) {
		    int r = h; //h / 2;
		    return new RoundRectangle2D.Double(x, y, w, h, r, r);
		  }
		  @Override 
		  public Insets getBorderInsets(Component c) {
		    return new Insets(4, 8, 4, 8);
		  }
		  @Override 
		  public Insets getBorderInsets(Component c, Insets insets) {
		    insets.set(4, 8, 4, 8);
		    return insets;
		  }
}

