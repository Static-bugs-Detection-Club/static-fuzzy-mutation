package buildtree;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToGlobal implements Mutation {
    public ArrayList<Node> main(ArrayList<Node> list) throws IOException, CoreException {

        ArrayList<Node> reNode = new ArrayList<>();

        for (int k = 0; k < list.size(); k++) {
            ArrayList<Node> nodes = new ArrayList<>();
            TreeUtils.getLocalNode(list.get(k), nodes);
            ArrayList<ArrayList<Integer>> comNumbers = Combination.getCombine(nodes.size());
            for (int i = 0; i < comNumbers.size(); i++) {
                ArrayList<Integer> num = comNumbers.get(i);
                Node pNode = SerializationUtils.clone(list.get(k));
                if (num.size() > 0) {
                    for (int j = 0; j < num.size(); j++) {
                        Node tempNode = nodes.get(num.get(j) - 1);
                        pNode.getSonList().add(j, tempNode.getSonList().get(0));
                        addGlobal(pNode, tempNode);
                    }
                }
                reNode.add(pNode);
            }
        }

        System.out.println(reNode.size());
        return reNode;
    }

    public static void addGlobal(Node pNode, Node tempNode) {
        List<Node> sonList = pNode.getSonList();

        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                if (sonList.get(i).getId().equals(tempNode.getId())) {
                    sonList.remove(i);
                    break;
                }
                addGlobal(sonList.get(i), tempNode);
            }
        }
    }
}
