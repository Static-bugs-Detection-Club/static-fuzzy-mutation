package buildtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;




public class TreeTest {

    public static void main(String[] args) throws Exception {
        IASTTranslationUnit u = getTranslationUnit(new File("C:\\Users\\eclipse\\Desktop\\test.txt"));
        //输出的是文件的所有内容
        System.out.println("simpleDeclaration.getRawSignature():"+u.getRawSignature());


        /**
         * 获取注释
         */
        IASTComment[]  com = u.getComments();
        for (IASTComment iastComment : com) {
            System.out.println("IASTComment:"+iastComment);
        }



        /**
         * 得到预处理语句
         * #include <queue>
         * #define maxnum 120   这一类
         */
        IASTPreprocessorStatement[] ps = u.getAllPreprocessorStatements();
        for (IASTPreprocessorStatement iastPreprocessorStatement : ps) {
            System.out.println(iastPreprocessorStatement.getRawSignature());
        }


        //得到文件中定义的声明
        IASTDeclaration[] decs = u.getDeclarations();
        for ( IASTDeclaration child : decs)
        {
            //方法声明
            if (child instanceof IASTFunctionDefinition)
            {


                //获得函数说明符,例void
                System.out.println(((IASTFunctionDefinition)child).getDeclSpecifier().getRawSignature());
                //获得函数的函数声明符 ,例Dijkstra(ALGraph g, int v0, int n)
                System.out.println(((IASTFunctionDefinition)child).getDeclarator().getRawSignature());
                //获得函数体的内容 {}之间的内容
                System.out.println(((IASTFunctionDefinition)child).getBody().getRawSignature());

                //输出函数的全部内容
                System.out.println(child.getRawSignature());

                //与函数的起始位置有关
                IASTFileLocation  FileLocation = ((IASTFunctionDefinition)child).getFileLocation();
                int startLine = FileLocation.getStartingLineNumber();
                int endLine = FileLocation.getEndingLineNumber();
                System.out.println("length:"+ (endLine-startLine));
            }
        }

    }

    /**
     * 创建解析单元
     * @param source
     * @return
     * @throws Exception
     */
    static IASTTranslationUnit getTranslationUnit(File source) throws Exception{
        FileContent reader = FileContent.create(
                source.getAbsolutePath(),
                getContentFile(source).toCharArray());


        //C++用GPPLanguage解析，C用GCCLanguage解析
        return GPPLanguage.getDefault().getASTTranslationUnit(
                reader,
                new ScannerInfo(),
                IncludeFileContentProvider.getSavedFilesProvider(),
                null,
                ILanguage.OPTION_IS_SOURCE_UNIT,
                new DefaultLogService());


//        return GCCLanguage.getDefault().getASTTranslationUnit(
//                reader,
//                new ScannerInfo(),
//                IncludeFileContentProvider.getSavedFilesProvider(),
//                null,
//                ILanguage.OPTION_IS_SOURCE_UNIT,
//                new DefaultLogService());
    }

    /**
     * 获得文件中的内容
     * @param file
     * @return
     * @throws IOException
     */
    static String getContentFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            while ((line = br.readLine()) != null)
                content.append(line).append('\n');
        }

        return content.toString();
    }
}
