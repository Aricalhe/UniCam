package unicam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Jelmerro
 */
public class SettingsPopup extends JPanel {

    private final DefaultComboBoxModel webcamModel;
    private final JComboBox webcamBox;
    private final JTextField XField;
    private final JTextField YField;

    public SettingsPopup() {
        webcamModel = new DefaultComboBoxModel();
        webcamBox = new JComboBox(webcamModel);
        Box resBox = new Box(BoxLayout.X_AXIS);
        XField = new JTextField();
        YField = new JTextField();
        webcamBox.addActionListener(e -> {
            Dimension d = getResolution();
            if (d != null) {
                XField.setText("" + d.width);
                YField.setText("" + d.height);
            }
        });
        Button doubleButton = new Button("2X");
        doubleButton.addActionListener(e -> {
            try {
                XField.setText("" + (Integer.parseInt(XField.getText()) * 2));
                YField.setText("" + (Integer.parseInt(YField.getText()) * 2));
            } catch (Exception ex) {

            }
        });
        Button halfButton = new Button("/2");
        halfButton.addActionListener(e -> {
            try {
                XField.setText("" + (Integer.parseInt(XField.getText()) / 2));
                YField.setText("" + (Integer.parseInt(YField.getText()) / 2));
            } catch (Exception ex) {

            }
        });
        resBox.add(doubleButton);
        resBox.add(XField);
        resBox.add(YField);
        resBox.add(halfButton);
        add(new JLabel("Webcam:"));
        add(webcamBox);
        add(new JLabel("Resolution:"));
        add(resBox);
        setLayout(new GridLayout(2, 2, 20, 20));
    }

    @Override
    public void show() {
        boolean loop = true;
        while (loop) {
            loop = false;
            int result = JOptionPane.showConfirmDialog(null, this, "UniCam", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Dimension dimension = new Dimension(Integer.parseInt(XField.getText()), Integer.parseInt(YField.getText()));
                    LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.load(webcamBox.getSelectedItem(), dimension);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please only use numbers", "Number warning", JOptionPane.WARNING_MESSAGE);
                    loop = true;
                }
            }
        }
    }

    public void refreshWebcams() {
        Webcam selectedWebcam = getCurrentWebcam();
        webcamModel.removeAllElements();
        for (Webcam webcam : Webcam.getWebcams()) {
            webcamModel.addElement(webcam);
        }
        if (selectedWebcam != null) {
            webcamModel.setSelectedItem(selectedWebcam);
        }
    }

    public Webcam getCurrentWebcam() {
        return (Webcam) webcamBox.getSelectedItem();
    }

    private Dimension getResolution() {
        if (Frame.getInstance().getPanel() != null) {
            return Frame.getInstance().getPanel().getPreferredSize();
        }
        return new Dimension(640, 480);
    }
}
