import java.util.Arrays;

public class TwoThreeTree {
    private Node root;
    private long operations;

    public TwoThreeTree() {
        root = null;
        operations = 0;
    }

    public void resetOps() { operations = 0; }
    public long getOps() { return operations; }
    private void addOp() { operations++; }

    // ==================== ПОИСК ====================
    public boolean search(int key) {
        return searchRecursive(root, key);
    }

    private boolean searchRecursive(Node node, int key) {
        addOp();
        if (node == null) return false;

        for (int i = 0; i < node.numKeys; i++) {
            addOp();
            if (key == node.keys[i]) return true;
        }

        if (node.isLeaf) return false;

        addOp();
        if (key < node.keys[0]) {
            return searchRecursive(node.children[0], key);
        } else if (node.numKeys == 1 || key < node.keys[1]) {
            return searchRecursive(node.children[1], key);
        } else {
            return searchRecursive(node.children[2], key);
        }
    }

    // ==================== ВСТАВКА ====================
    public void insert(int key) {
        if (root == null) {
            addOp();
            root = new Node();
            root.keys[0] = key;
            root.numKeys = 1;
            return;
        }

        SplitResult result = insertRecursive(root, key);

        if (result != null) {
            addOp();
            Node newRoot = new Node();
            newRoot.keys[0] = result.midKey;
            newRoot.numKeys = 1;
            newRoot.isLeaf = false;
            newRoot.children[0] = result.left;
            newRoot.children[1] = result.right;
            root = newRoot;
        }
    }

    private SplitResult insertRecursive(Node node, int key) {
        addOp();

        if (node.isLeaf) {
            for (int i = 0; i < node.numKeys; i++) {
                addOp();
                if (node.keys[i] == key) return null;
            }

            if (node.numKeys == 1) {
                addOp();
                if (key < node.keys[0]) {
                    node.keys[1] = node.keys[0];
                    node.keys[0] = key;
                } else {
                    node.keys[1] = key;
                }
                node.numKeys = 2;
                return null;
            }

            return splitNode(node, key, null);
        }

        SplitResult childSplit = null;
        addOp();
        if (key < node.keys[0]) {
            childSplit = insertRecursive(node.children[0], key);
        } else if (node.numKeys == 1 || key < node.keys[1]) {
            childSplit = insertRecursive(node.children[1], key);
        } else {
            childSplit = insertRecursive(node.children[2], key);
        }

        if (childSplit != null) {
            if (node.numKeys == 1) {
                addOp();
                if (childSplit.midKey < node.keys[0]) {
                    node.keys[1] = node.keys[0];
                    node.keys[0] = childSplit.midKey;
                    node.children[2] = node.children[1];
                    node.children[1] = childSplit.right;
                    node.children[0] = childSplit.left;
                } else {
                    node.keys[1] = childSplit.midKey;
                    node.children[2] = childSplit.right;
                    node.children[1] = childSplit.left;
                }
                node.numKeys = 2;
                return null;
            } else {
                return splitNode(node, childSplit.midKey, childSplit);
            }
        }
        return null;
    }

    private SplitResult splitNode(Node node, int extraKey, SplitResult childSplit) {
        addOp();
        int[] tempKeys = new int[3];
        Node[] tempChildren = new Node[4];

        tempKeys[0] = node.keys[0];
        tempKeys[1] = node.keys[1];
        tempKeys[2] = extraKey;

        if (childSplit != null) {
            if (extraKey < node.keys[0]) {
                tempChildren[0] = childSplit.left; tempChildren[1] = childSplit.right;
                tempChildren[2] = node.children[1]; tempChildren[3] = node.children[2];
            } else if (extraKey < node.keys[1]) {
                tempChildren[0] = node.children[0]; tempChildren[1] = childSplit.left;
                tempChildren[2] = childSplit.right; tempChildren[3] = node.children[2];
            } else {
                tempChildren[0] = node.children[0]; tempChildren[1] = node.children[1];
                tempChildren[2] = childSplit.left; tempChildren[3] = childSplit.right;
            }
        }

        Arrays.sort(tempKeys);
        int mid = tempKeys[1];

        Node left = new Node();
        Node right = new Node();
        left.isLeaf = node.isLeaf;
        right.isLeaf = node.isLeaf;

        left.keys[0] = tempKeys[0];
        left.numKeys = 1;
        right.keys[0] = tempKeys[2];
        right.numKeys = 1;

        if (childSplit != null) {
            left.children[0] = tempChildren[0]; left.children[1] = tempChildren[1];
            right.children[0] = tempChildren[2]; right.children[1] = tempChildren[3];
        }

        return new SplitResult(left, right, mid);
    }

    // ==================== УДАЛЕНИЕ ====================
    public void delete(int key) {
        addOp();
        if (root == null) return;
        deleteRecursive(root, null, key);
        if (root != null && root.numKeys == 0) root = null;
    }

    private boolean deleteRecursive(Node node, Node parent, int key) {
        addOp();
        if (node == null) return false;

        boolean found = false;
        int i;
        for (i = 0; i < node.numKeys; i++) {
            addOp();
            if (key == node.keys[i]) {
                found = true;
                break;
            }
        }

        if (found) {
            if (node.isLeaf) {
                addOp();
                if (node.numKeys == 2) {
                    if (i == 0) node.keys[0] = node.keys[1];
                    node.numKeys = 1;
                } else {
                    node.numKeys = 0;
                }
                return true;
            } else {
                addOp();
                Node successor = node.children[i + 1];
                while (!successor.isLeaf) {
                    addOp();
                    successor = successor.children[0];
                }
                int replacement = successor.keys[0];
                node.keys[i] = replacement;
                return deleteRecursive(node.children[i + 1], node, replacement);
            }
        } else {
            if (node.isLeaf) return false;
            addOp();
            if (key < node.keys[0]) return deleteRecursive(node.children[0], node, key);
            else if (node.numKeys == 1 || key < node.keys[1]) return deleteRecursive(node.children[1], node, key);
            else return deleteRecursive(node.children[2], node, key);
        }
    }
}
