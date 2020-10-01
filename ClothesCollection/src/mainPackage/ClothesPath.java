package mainPackage;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class ClothesPath {

	public String url;
	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClothesPath window = new ClothesPath();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClothesPath() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogType(fileChooser.SAVE_DIALOG);
		frame.add(fileChooser);

		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getActionCommand().equals("ApproveSelection")) {
					url = fileChooser.getSelectedFile().getAbsolutePath();
				} else if (evt.getActionCommand().equals("CancelSelection")) {

				}
				frame.dispose();
			}
		});
	}
}
