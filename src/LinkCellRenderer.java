import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * ��ʾ�����ӵ�JTable��Ԫ����Ⱦ��
 * @author ��ʤ��
 * @date 2013-06-20 16:08:36
 */
public class LinkCellRenderer extends DefaultTableCellRenderer implements MouseInputListener {

    //����¼����ڵ���
    private int row = -1;
    //����¼����ڵ���
    private int col = -1;
    //��ǰ������Table
    private JTable table = null;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //�ָ�Ĭ��״̬
        this.table = table;
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setForeground(Color.black);
        table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.setText(value.toString());
        //�����ǰ��Ҫ��Ⱦ���ĵ�Ԫ���������¼����ڵĵ�Ԫ��
        if (row == this.row && column == this.col) {
            //����ǵڶ���(�ڶ�������ʾ�����ӵ���)
            if (column == 1) {
                //�ı�ǰ��ɫ(������ɫ)
                this.setForeground(Color.blue);
                //�ı������״
                table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                //��ʾ��������ʽ
                this.setText("<html><u>" + value.toString() + "</u></html>");
            }
            setBackground(table.getSelectionBackground());
        } else if (isSelected) {
            //�����Ԫ��ѡ��,��ı�ǰ��ɫ�ͱ���ɫ
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            //��������»ָ�Ĭ�ϱ���ɫ
            setBackground(Color.white);
        }
        return this;
    }

    /**
     * ����Ƴ��¼�
     * @param e 
     */
    public void mouseExited(MouseEvent e) {
        if (table != null) {
            int oldRow = row;
            int oldCol = col;
            //����Ƴ�Ŀ�����,�ָ��������ݵ�Ĭ��ֵ
            row = -1;
            col = -1;
            //��֮ǰ������������Чʱ�ػ��������
            if (oldRow != -1 && oldCol != -1) {
                Rectangle rect = table.getCellRect(oldRow, oldCol, false);
                table.repaint(rect);
            }
        }
    }

    /**
     * ����϶��¼�
     * @param e 
     */
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * ����ƶ��¼�
     * @param e 
     */
    public void mouseMoved(MouseEvent e) {
        if (table != null) {
            Point p = e.getPoint();
            int oldRow = row;
            int oldCol = col;
            row = table.rowAtPoint(p);
            col = table.columnAtPoint(p);
            //�ػ�ԭ��������
            if (oldRow != -1 && oldCol != -1) {
                Rectangle rect = table.getCellRect(oldRow, oldCol, false);
                table.repaint(rect);
            }
            //�ػ��µ�����
            if (row != -1 && col != -1) {
                Rectangle rect = table.getCellRect(row, col, false);
                table.repaint(rect);
            }
        }
    }

    /**
     * ��굥���¼�
     * @param e 
     */
    public void mouseClicked(MouseEvent e) {
        //��ȡ�¼����ڵ�����������Ϣ
        Point p = e.getPoint();
        int c = table.columnAtPoint(p);
        if(c != 1){
            return;
        }
        int r = table.rowAtPoint(p);
        try {
            //ȡ��Ŀ�굥Ԫ���ֵ,��������Ϣ
            URL url = new URL(table.getValueAt(r, c).toString());
            //��ϵͳĬ��������д�����
            Desktop.getDesktop().browse(url.toURI());
        } catch (Exception ex) {
            Logger.getLogger(LinkCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ��갴���¼�
     * @param e 
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * ����ͷ��¼�
     * @param e 
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * �������¼�
     * @param e 
     */
    public void mouseEntered(MouseEvent e) {
    }

    
    /**
     * ���Է���
     * @param args 
     */
    public static void main(String[] args) {
        //���ڱ���г��ֵ�����
        Object[] header = new String[]{"����", "����"};
        Object[][] data = new String[10][2];
        for (int i = 0; i < 10; i++) {
            data[i][0] = "��ҳ����";
            data[i][1] = "http://www.yshjava.cn/post/529.html";
        }
        //�����������ģ��
        TableModel model = new DefaultTableModel(data, header);
        //����������
        JTable table = new JTable(model);
        //������Ԫ����Ⱦ��������¼�������
        LinkCellRenderer renderer = new LinkCellRenderer();
        //ע����Ⱦ��
        table.setDefaultRenderer(Object.class, renderer);
        //ע�������
        table.addMouseListener(renderer);
        table.addMouseMotionListener(renderer);

        //Ϊ������Ӱ���������
        JScrollPane sp = new JScrollPane(table);
        //�������ڳ���
        JFrame f = new JFrame("JTable ��Ԫ�����Ӳ���");
        f.getContentPane().add(sp, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        //��ʾ����
        f.setVisible(true);
    }
}