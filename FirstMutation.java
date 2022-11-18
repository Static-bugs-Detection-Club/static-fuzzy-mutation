import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;

public class FirstMutation {
    private static boolean fileLog = true;

    public static OutputStream getOutputStream(String str) throws IOException {
        if (fileLog) {
            File file = new File("C:\\Users\\eclipse\\Desktop\\数据\\变异\\ASTResult2\\" + str+".txt");
            if (!file.exists())
                file.createNewFile();
            return new FileOutputStream(file, true);
        } else {
            return System.out;
        }
    }

    public static void log(String info, String str) throws IOException {
        OutputStream out = getOutputStream(str);
        out.write(info.getBytes("utf-8"));
        out.write("\r\n".getBytes());

    }
    public static void getAst(IASTTranslationUnit translationUnit, ArrayList<ArrayList<String>> lists, ArrayList<Integer> list, String str, ArrayList<String> secondList) throws IOException {
        printTree(translationUnit, 1, lists, list, str);
        for (int i = 0; i < secondList.size(); i++) {
            log(secondList.get(i),str);
        }
    }

    private static void printTree(IASTNode node, int index, ArrayList<ArrayList<String>> lists,ArrayList<Integer> list, String fileName) throws IOException {
        IASTNode[] children = node.getChildren();

        boolean printContents = true;

        if ((node instanceof CASTTranslationUnit)) {
            printContents = false;
        }

        String offset = "";
        try {
            offset = node.getSyntax() != null ? " (offset: " + node.getFileLocation().getNodeOffset() + "," + node.getFileLocation().getNodeLength() + ")" : "";
            printContents = node.getFileLocation().getNodeLength() < 30;
        } catch (ExpansionOverlapsBoundaryException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            offset = "UnsupportedOperationException";
        }

        String string = String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                node.getClass().getSimpleName() + "  " +
                (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                        node.getRawSignature().subSequence(0, 5));

        if(node.getClass().getSimpleName().equals("CASTTranslationUnit")){
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 5)), fileName);
            int n = list.size();
            for(int i = 0; i < n; i++){
                ArrayList<String> list1 = lists.get(list.get(i) - 1);
                String str = list1.get(1);
                int l1 = str.length();
                String str1 = str.trim();
                int l2 = str1.length();
                int gap = l1 - l2;
                log("    " + str.trim(), fileName);
                for(int j = 2; j < list1.size(); j++){
                    StringBuilder sb = new StringBuilder();
                    String str2 = list1.get(j);
                    int l3 = str2.length();
                    String str3 = str2.trim();
                    int l4 = str3.length();
                    int gap1 = l3 - l4;
                    int dif = gap1 - gap;
                    for(int m = 0; m < dif; m++)
                        sb.append(" ");
                    log("    " + sb + str3, fileName);
                }
            }
        }
//        if (node.getClass().getSimpleName().equals("CASTCompoundStatement")) {
//            System.out.println(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
//                    node.getClass().getSimpleName() + "  " +
//                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
//                            node.getRawSignature().subSequence(0, 1)));
//        }
//        else if (node.getClass().getSimpleName().equals("CASTDeclarationStatement")) {
//            if(!lists.isEmpty()){
//                for (int i = 0; i < lists.size(); i++){
//                    for(int j = 0; j < lists.get(i).size(); j++){
//                        System.out.println(lists.get(i).get(j));
//                    }
//                    lists.remove(i);
//                }
//            }
//        }
        else if (node.getClass().getSimpleName().equals("CASTCompoundStatement")) {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 1)), fileName);
        }
        else {
            boolean bool = true;
            for(int i = 0; i < list.size(); i++){
                ArrayList<String> list1 = lists.get(list.get(i) - 1);
                if(list1.get(0).equals(string))
                    bool = false;
            }
            if(bool){
                log(string, fileName);

            }else {
                return;
            }
        }

        for (IASTNode iastNode : children)
            printTree(iastNode, index + 1, lists, list, fileName);
    }


}
