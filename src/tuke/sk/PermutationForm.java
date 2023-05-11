package tuke.sk;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermutationForm {
    private JButton permutationButton;
    private JPanel permutationPanel;
    private JLabel permutationLabel;
    private JSpinner permutationSpinner;
    private JScrollPane scrollPane;
    private JTextArea permutationTextArea;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton calculateButton;

    private boolean running = false;

    public PermutationForm() {
        permutationButton.addActionListener(e -> {
            permutationTextArea.selectAll();
            permutationTextArea.replaceSelection("");
            permutationTextArea.setMargin(new Insets(10, 10, 10, 10));
                final Task task = new Task();
                task.addPropertyChangeListener(pce -> {});
                task.execute();
        });
        calculateButton.addActionListener(e -> {
            permutationTextArea.selectAll();
            permutationTextArea.replaceSelection("");
            permutationTextArea.setMargin(new Insets(10, 10, 10, 10));
            List<List<Integer>> inputData = new ArrayList<>();
            inputData.add(inputToList(textField1.getText()));
            inputData.add(inputToList(textField2.getText()));
            inputData.add(inputToList(textField3.getText()));
            inputData.add(inputToList(textField4.getText()));
            int[][] result = createDistanceTable(inputData);
            printDistanceTable(result);
            calculateButton.setMargin(new Insets(0, 0, 10, 0));
        });
    }

    private void printDistanceTable(int[][] result) {
        StringBuilder sb = new StringBuilder("\tA1\tA2\tA3\tA4" + System.lineSeparator() + System.lineSeparator());
        for (int i = 0; i < 4; i++) {
            sb.append("A" + (i+1));
            for (int j = 0; j < 4; j++) {
                sb.append("\t" + result[i][j]);
            }
            sb.append(System.lineSeparator() + System.lineSeparator());
        }
        permutationTextArea.append(sb.toString());
    }

    private int[][] createDistanceTable(List<List<Integer>> inputData) {
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                int partialResult = calculateDistance(new ArrayList<>(inputData.get(i)), new ArrayList<>(inputData.get(j)), true);
                if (i == j) {
                    result[i][j] =  partialResult;
                } else {
                    result[i][j] = partialResult;
                    result[j][i] = partialResult;
                }
            }
        }
        return result;
    }

    private int calculateDistance(List<Integer> a, List<Integer> b, boolean removeSame) {
        if (a.size() <= 1) {
            return 0;
        } else {
            if (removeSame) {
                for (int i = 0; i < a.size(); i++) {
                    if (a.get(i) == b.get(i)) {
                        a.remove(i);
                        b.remove(i);
                        return calculateDistance(a, b, true);
                    }
                }
            }

            int first = a.get(0);
            for (int i = 0; i < b.size(); i++) {
                if (first == b.get(i)) {
                    a.remove(0);
                    b.remove(i);
                    return (i - 0) + calculateDistance(a, b, false);
                }
            }
        }
        return 0;
    }

    private List<Integer> reverseList(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();
        newList.add(1); // list always start with 0
        for (int i = list.size() - 1; i >= 1; i--) {
            newList.add(list.get(i));
        }
        return newList;
    }

    private List<Integer> inputToList(String input) {
        String replace = input.replace("[","").replace("]","");
        List<String> asText = new ArrayList<String> (Arrays.asList(replace.split(",")));
        List<Integer> list = new ArrayList<Integer>();
        for(String fav:asText){
            list.add(Integer.parseInt(fav.trim()));
        }
        return list;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Permutation");
        frame.setContentPane(new PermutationForm().permutationPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.pack();
        frame.setVisible(true);
    }

    void permute(int[] a, int k) {
        if (k == a.length) {
            printPermutation(a);
        } else {
            for (int i = k; i < a.length; i++) {
                int temp = a[k];
                a[k] = a[i];
                a[i] = temp;
                permute(a, k + 1);
                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    }

    void printPermutation(int[] a) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ 1, ");
        for (int i = 0; i < a.length; i++) {
            if (i == a.length - 1) {
                sb.append(" " + a[i] + " ");
            } else {
                sb.append(" " + a[i] + ", ");
            }
        }
        sb.append("]" + System.lineSeparator());
        permutationTextArea.append(sb.toString());
    }

    private class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            if (!running) {
                running = true;
                int N = (Integer) permutationSpinner.getValue();
                if (N > 1) {
                    int[] sequence = new int[N-1];
                    for (int i = 0; i < N-1; i++) {
                        sequence[i] = i + 2;
                    }
                    permute(sequence, 0);
                    running = false;
                }
            }
            return null;
        }
    }
}
