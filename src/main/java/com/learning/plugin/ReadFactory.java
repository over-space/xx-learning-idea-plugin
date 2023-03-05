package com.learning.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.learning.plugin.ui.ReadUI;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ReadFactory implements ToolWindowFactory {

    private static ReadUI readUI = new ReadUI();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // 获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        // 获取 ToolWindow 显示的内容
        Content content = contentFactory.createContent(readUI.getComponent(), "", false);

        // 设置 ToolWindow 显示的内容
        toolWindow.getContentManager().addContent(content);

        // 全局使用
        // Config.readUI = readUI;

        try {
            String text = readFile(new File("C:\\Users\\Lee\\Desktop\\1.md"));
            setText(text);


            // https://mvnrepository.com/artifact/com.atlassian.commonmark/commonmark-ext-gfm-tables

            // List<Extension> extensions = Arrays.asList(TablesExtension.create());
            // Parser parser = Parser.builder()
            //         .extensions(extensions)
            //         .build();
            // HtmlRenderer renderer = HtmlRenderer.builder()
            //         .extensions(extensions)
            //         .build();
            // Node parse = parser.parse(str);



            readUI.getNextButton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String text = readFile(new File("C:\\Users\\Lee\\Desktop\\2.md"));
                        setText(text);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            readUI.getPrevButton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String text = readFile(new File("C:\\Users\\Lee\\Desktop\\1.md"));
                        setText(text);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();;
        }
    }

    private String readFile(File file) throws IOException {
        byte[] bytes = new byte[1024 * 1024];

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(0);
        int readSize = randomAccessFile.read(bytes);

        byte[] copy = new byte[readSize];
        System.arraycopy(bytes, 0, copy, 0, readSize);

        return new String(copy, StandardCharsets.UTF_8);
    }

    private void setText(String content){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String render = renderer.render(document);

        // 设置内容
        readUI.getTextContent().setText(render);
    }

}
