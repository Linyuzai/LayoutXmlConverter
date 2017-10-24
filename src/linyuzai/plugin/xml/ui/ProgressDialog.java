package linyuzai.plugin.xml.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class ProgressDialog extends JDialog {
    private JPanel contentPane;
    private JProgressBar progressBar;

    public ProgressDialog(int max) {
        this();
        progressBar.setMaximum(max);
    }

    private ProgressDialog() {
        //contentPane.setLayout(new GridLayoutManager(1, 1));
        //System.out.println(progressBar);
        //contentPane.add(progressBar);
        //setSize(1000, 100);
        setTitle("Convert xml to Kotlin Anko");
        setResizable(false);
        setAlwaysOnTop(true);

        setContentPane(contentPane);
        GridConstraints constraints = new GridConstraints();
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        constraints.setVSizePolicy(GridConstraints.ALIGN_CENTER);
        //constraints.setUseParentLayout();
        getContentPane().add(progressBar, constraints);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        //setModal(true);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e ->
                        onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void onCancel() {
        // add your code here if necessary
        System.out.println("cancel");
        //dispose();
    }

    public void updateProgress(int value) {
        progressBar.setValue(value);
        disposeIfFinish();
    }

    public void increaseProgress(int value) {
        progressBar.setValue(progressBar.getValue() + value);
        disposeIfFinish();
    }

    private void disposeIfFinish() {
        if (progressBar.getValue() == progressBar.getMaximum())
            dispose();
    }

    public String getValue() {
        return progressBar.getValue() + "";
    }

    public String getTotal() {
        return progressBar.getMaximum() + "";
    }
}
