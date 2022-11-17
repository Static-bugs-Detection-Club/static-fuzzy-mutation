package buildtree;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.core.runtime.CoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTree {

    public static Node getTree(String path) throws CoreException {

        IASTTranslationUnit node = getAST(path);

        String name = node.getClass().getSimpleName();
        String value = node.getRawSignature();
        String id = name + value;
        Node curNode = new Node(id, name, value);
        List<Node> list = new ArrayList<>();
        curNode.setSonList(list);
        IASTNode[] children = node.getChildren();
        for (IASTNode child: children) {
            buildTree(curNode, child);
        }
        return curNode;
    }

    public static void buildTree(Node rootNode, IASTNode iastNode){
        IASTNode[] children = iastNode.getChildren();

        String fatherName = iastNode.getParent().getClass().getSimpleName();
        String fatherValue = iastNode.getParent().getRawSignature();
        String fatherId = fatherName + fatherValue;
        String name = iastNode.getClass().getSimpleName();
        String value = iastNode.getRawSignature();
        String id = name + value;
        Node curNode = new Node(id, name, value);
        List<Node> list = new ArrayList<>();
        curNode.setSonList(list);
        TreeUtils.insert(rootNode, fatherId, curNode);
        for (IASTNode child: children) {
            buildTree(rootNode, child);
        }
    }

    public static IASTTranslationUnit getAST(String path) throws CoreException {
        FileContent fileContent = FileContent.createForExternalFileLocation(path);

        Map definedSymbols = new HashMap();
        String[] includePaths = new String[0];
        IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
        IParserLogService log = new DefaultLogService();

        IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

        int opts = 8;
        IASTTranslationUnit translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);

        return translationUnit;
    }

    public static ArrayList<String> getMainName(IASTTranslationUnit translationUnit){
        ArrayList<String> list = new ArrayList<>();
        ASTVisitor visitor = new ASTVisitor() {
            public int visit(IASTName name) {
                if ((name.getParent() instanceof CASTFunctionDeclarator)) {
                    list.add(name.getRawSignature());
//                    System.out.println("IASTName: " + name.getClass().getSimpleName() + "(" + name.getRawSignature() + ") - > parent: " + name.getParent().getClass().getSimpleName());
//                    System.out.println("-- isVisible: " + ParserExample.isVisible(name));
                }

                return 3;
            }

//            public int visit(IASTDeclaration declaration) {
//                System.out.println("declaration: " + declaration + " ->  " + declaration.getRawSignature());
//
//                if ((declaration instanceof IASTSimpleDeclaration)) {
//                    IASTSimpleDeclaration ast = (IASTSimpleDeclaration) declaration;
//                    try {
//                        System.out.println("--- type: " + ast.getSyntax() + " (childs: " + ast.getChildren().length + ")");
//                        IASTNode typedef = ast.getChildren().length == 1 ? ast.getChildren()[0] : ast.getChildren()[1];
//                        System.out.println("------- typedef: " + typedef);
//                        IASTNode[] children = typedef.getChildren();
//                        if ((children != null) && (children.length > 0))
//                            System.out.println("------- typedef-name: " + children[0].getRawSignature());
//                    } catch (ExpansionOverlapsBoundaryException e) {
//                        e.printStackTrace();
//                    }
//
//                    IASTDeclarator[] declarators = ast.getDeclarators();
//                    for (IASTDeclarator iastDeclarator : declarators) {
//                        System.out.println("iastDeclarator > " + iastDeclarator.getName());
//                    }
//
//                    IASTAttribute[] attributes = ast.getAttributes();
//                    for (IASTAttribute iastAttribute : attributes) {
//                        System.out.println("iastAttribute > " + iastAttribute);
//                    }
//
//                }
//
//                if ((declaration instanceof IASTFunctionDefinition)) {
//                    IASTFunctionDefinition ast = (IASTFunctionDefinition) declaration;
//                    IScope scope = ast.getScope();
//                    try {
//                        System.out.println("### function() - Parent = " + scope.getParent().getScopeName());
//                        System.out.println("### function() - Syntax = " + ast.getSyntax());
//                    } catch (DOMException e) {
//                        e.printStackTrace();
//                    } catch (ExpansionOverlapsBoundaryException e) {
//                        e.printStackTrace();
//                    }
//                    ICPPASTFunctionDeclarator typedef = (ICPPASTFunctionDeclarator) ast.getDeclarator();
//                    System.out.println("------- typedef: " + typedef.getName());
//                }
//
//                return 3;
//            }
//
//            public int visit(IASTTypeId typeId) {
//                System.out.println("typeId: " + typeId.getRawSignature());
//                return 3;
//            }
//
//            public int visit(IASTStatement statement) {
//                System.out.println("statement: " + statement.getRawSignature());
//                return 3;
//            }
//
//            public int visit(IASTAttribute attribute) {
//                return 3;
//            }
        };
        visitor.shouldVisitNames = true;
        visitor.shouldVisitDeclarations = false;

        visitor.shouldVisitDeclarators = true;
        visitor.shouldVisitAttributes = true;
        visitor.shouldVisitStatements = false;
        visitor.shouldVisitTypeIds = true;

        translationUnit.accept(visitor);
        return list;
    }
}
