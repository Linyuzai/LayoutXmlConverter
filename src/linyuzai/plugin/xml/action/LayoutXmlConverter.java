package linyuzai.plugin.xml.action;

import com.intellij.ide.MacOSApplicationProvider;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import linyuzai.plugin.xml.attr.UApplication;
import linyuzai.plugin.xml.common.AttributeUtil;
import linyuzai.plugin.xml.common.ConvertUtil;
import linyuzai.plugin.xml.common.FileUtil;
import linyuzai.plugin.xml.common.ImportUtil;
import linyuzai.plugin.xml.converter.AnkoConverter;
import linyuzai.plugin.xml.ui.ProgressDialog;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;

public class LayoutXmlConverter extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
        if (file != null && file.getName().endsWith(XmlFileType.DOT_DEFAULT_EXTENSION)) {
            PsiDirectory parent1 = file.getParent();
            if (parent1 != null && parent1.getName().equals("layout")) {
                PsiDirectory parent2 = parent1.getParent();
                if (parent2 != null && parent2.getName().equals("res")) {
                    e.getPresentation().setEnabled(true);
                } else {
                    e.getPresentation().setEnabled(false);
                }
            } else {
                e.getPresentation().setEnabled(false);
            }
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        //e.getProject()


        //ApplicationManager.getApplication()


        FileDocumentManager.getInstance().saveAllDocuments();
        ImportUtil.clearImport();
        //ProgressBar progressBar = new ProgressBar();
        //progressBar.setVisible(true);
        //PsiManager manager =
        PsiFile files = e.getData(CommonDataKeys.PSI_FILE);
        if (files == null) {
            Messages.showMessageDialog("Nothing is Chosen", "Info", null);
            return;
        }

        VirtualFile virtualFile = files.getVirtualFile();
        //ApplicationManager.getApplication().runWriteAction(()->virtualFile.refresh(false, false));
        //virtualFile

        Document document;
        try {
            document = new SAXReader().read(virtualFile.getInputStream());
            //document.nodeCount()
        } catch (IOException | DocumentException id) {
            Messages.showMessageDialog("Xml Parse Failure", "Error", null);
            id.printStackTrace();
            return;
        }
        if (document == null) {
            Messages.showMessageDialog("Nothing in Xml", "Info", null);
            return;
        }
        int count = AttributeUtil.countOfElement(document.getRootElement());
        ProgressDialog dialog = new ProgressDialog(count * 2 + 2);//manifest+file

        Element root = document.getRootElement();
        String className = ConvertUtil.convertClassName(files.getName().split(".xml")[0]);
        File ktFile;
        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog("Project is Null", "Error", null);
            return;
        }
        String manifest = project.getBasePath() + "/app/src/main/AndroidManifest.xml";
        //VirtualFileManager.getInstance().findFileByUrl()
        UApplication application = new UApplication();
        try {
            VirtualFile virtualManifest = LocalFileSystem.getInstance().findFileByPath(manifest);
            InputStream is = null;
            if (virtualManifest != null)
                is = virtualManifest.getInputStream();
            application = ConvertUtil.convertManifest(is);
        } catch (IOException io) {
            io.printStackTrace();
        }
        dialog.increaseProgress(1);
        String code = new AnkoConverter(application, new ProgressCallback(dialog)).convert(className, root);

        try {
            File dir = FileUtil.createDir(project.getBasePath() + "/app/src/main/java/anko");
            ktFile = FileUtil.createFile(dir, className + ".kt");
        } catch (IOException io) {
            Messages.showMessageDialog("File Create Failure", "Error", null);
            return;
        }
        if (ktFile == null) {
            Messages.showMessageDialog("File Create Failure", "Error", null);
            return;
        }

        try {
            FileUtil.writeFile(ktFile, code);
        } catch (IOException io) {
            Messages.showMessageDialog("File Write Failure", "Error", null);
            return;
        }
        dialog.increaseProgress(1);
        //Messages.showMessageDialog(code, "Info", null);
        //FileDocumentManager.getInstance().reloadFiles(virtualFile);

        //VirtualFile newFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(ktFile);//.getFileSystem().refresh(true);
//        if (newFile != null)
//            newFile.getFileSystem().refresh(true);
        //ApplicationManager.getApplication().runWriteAction(() -> {
            //LocalFileSystem.getInstance().refresh(false);
            //virtualFile.getFileSystem().refresh(false);
        //});
        //virtualFile.getFileSystem()
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(ktFile);
        VirtualFileManager.getInstance().syncRefresh();

        //progressBar.setVisible(false);
        //dialog.dispose();
    }

    private class ProgressCallback implements AnkoConverter.Callback {

        private ProgressDialog dialog;

        ProgressCallback(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onUpdate(int value) {
            //dialog.updateProgress(value);
            dialog.increaseProgress(1);
            //Messages.showMessageDialog(dialog.getValue(), dialog.getTotal(), null);
        }
    }
}
