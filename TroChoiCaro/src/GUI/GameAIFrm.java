/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;
//import controller.Client;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Point;
import model.XOButton;
/**
 *
 * @author Administrator
 */
public class GameAIFrm extends javax.swing.JFrame {
   private int row =20;
    private int col = 20;
    public XOButton[][] Buttons = new XOButton[col][row];
    private ArrayList<Point> availablesPoint = new ArrayList<Point>();
    private static final int winScore = 100000000;
    private int gameNumber = 0;
    private int userWin = 0;
    private int aIWin = 0;
    XOButton preButton;
    /**
     * Creates new form GameAIFrm
     */
    public GameAIFrm() {
        initComponents();
        this.setTitle("Caro Game Nhóm 5");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        jLabel12.setText("Thuajn");
        jLabel13.setText("11");
        jLabel14.setText("1");
        jLabel19.setIcon(new ImageIcon("./src/Photo//icons8_round_24px.png"));
        jLabel22.setIcon(new ImageIcon("assets/game/swords-1.png"));
        jLabel3.setIcon(new ImageIcon("assets/image/x3.jpg"));
        jLabel5.setIcon(new ImageIcon("assets/image/o3.jpg"));
        jButton3.setIcon(new ImageIcon("assets/image/ai.png"));
        //Set layout dạng lưới cho panel chứa button
        jPanel1.setLayout(new GridLayout(20, 20));
        //Setup play button
        for (int i = 0; i < Buttons.length; i++) {
            for (int j = 0; j < Buttons.length; j++) {
                Point point = new Point(i, j);
                Buttons[i][j] = new XOButton(i, j);
                Buttons[i][j].addMouseListener( new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // TODO Auto-generated method stub
                        handleClickButton(point);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }
                });
                jPanel1.add(Buttons[i][j]);
                availablesPoint.add(point);
            }
        }
        gameNumber++;
        preButton=null;
        
        
    }
    
    
   private void updateScore(){
        jLabel20.setText("Tỉ số: "+userWin+"-"+aIWin);
    }
    private void handleClickButton(Point point) {

        // TODO: CALC LOGIC HERE
        point.log();
        Buttons[point.x][point.y].setState(true);
        Buttons[point.x][point.y].setEnabled(false);
        if (getScore(getMatrixBoard(), true, false) >= winScore) {
            JOptionPane.showMessageDialog(null, "Bạn đã thắng");
            userWin++;
            updateScore();
            displayUserWin();
            newGame();
            return;
        }

        int nextMoveX = 0, nextMoveY = 0;
        int[] bestMove = calcNextMove(3);

        if (bestMove != null) {
            nextMoveX = bestMove[0];
            nextMoveY = bestMove[1];
        }
        Buttons[nextMoveX][nextMoveY].setState(false);
        Buttons[nextMoveX][nextMoveY].setEnabled(false);
        if (getScore(getMatrixBoard(), false, true) >= winScore) {
            JOptionPane.showMessageDialog(null, "Bạn đã thua");
            aIWin++;
            updateScore();
            displayAIWin();
            newGame();
        }
    }

    private void newGame(){
        for (int i = 0; i < Buttons.length; i++) {
            for (int j = 0; j < Buttons.length; j++) {
                Buttons[i][j].resetState();
            }
        }
        
        gameNumber++;
        if (gameNumber % 2 == 0) {
            JOptionPane.showMessageDialog(rootPane, "Máy đi trước", "Ván mới",JOptionPane.INFORMATION_MESSAGE);
            Buttons[9][9].setState(false);
            Buttons[9][9].setEnabled(false);

            if (getScore(getMatrixBoard(), false, true) >= winScore) {
                JOptionPane.showMessageDialog(null, "Bạn đã thua");
                newGame();
            }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Bạn đi trước", "Ván mới",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void displayUserWin(){
        String tmp = jTextArea1.getText();
        tmp+="--Bạn đã thắng, tỉ số hiện tại là "+userWin+"-"+aIWin+"--\n";
        jTextArea1.setText(tmp);
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }
    private void displayAIWin(){
        String tmp = jTextArea1.getText();
        tmp+="--Máy thắng, tỉ số hiện tại là "+userWin+"-"+aIWin+"--\n";
        jTextArea1.setText(tmp);
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }
    

    public int[] calcNextMove(int depth) {
        int[][] board = getMatrixBoard();
        Object[] bestMove = searchWinningMove(board);
        Object[] badMove = searchLoseMove(board);

        int[] move = new int[2];

        if (badMove[1] != null && badMove[2] != null) {

            move[0] = (Integer) (badMove[1]);
            move[1] = (Integer) (badMove[2]);
            return move;
        }

        if (bestMove[1] != null && bestMove[2] != null) {

            move[0] = (Integer) (bestMove[1]);
            move[1] = (Integer) (bestMove[2]);

        } else {

            bestMove = minimaxSearchAB(depth, board, true, -1.0, winScore);
            if (bestMove[1] == null) {
                move = null;
            } else {
                move[0] = (Integer) (bestMove[1]);
                move[1] = (Integer) (bestMove[2]);
            }
        }
        return move;
    }

    public int[][] playNextMove(int[][] board, int[] move, boolean isUserTurn) {
        int i = move[0], j = move[1];
        int[][] newBoard = new int[row][col];
        for (int h = 0; h < row; h++) {
            for (int k = 0; k < col; k++) {
                newBoard[h][k] = board[h][k];
            }
        }
        newBoard[i][j] = isUserTurn ? 2 : 1;
        return newBoard;
    }

    private Object[] searchWinningMove(int[][] matrix) {
        ArrayList<int[]> allPossibleMoves = generateMoves(matrix);
        System.out.println(allPossibleMoves.size());

        Object[] winningMove = new Object[3];

        for (int[] move : allPossibleMoves) {
            int[][] dummyBoard = playNextMove(matrix, move, false);

            // If the white player has a winning score in that temporary board, return the move.
            if (getScore(dummyBoard, false, false) >= winScore) {
                winningMove[1] = move[0];
                winningMove[2] = move[1];
                return winningMove;
            }
        }

        return winningMove;
    }

    private Object[] searchLoseMove(int[][] matrix) {
        ArrayList<int[]> allPossibleMoves = generateMoves(matrix);
        System.out.println(allPossibleMoves.size());

        Object[] losingMove = new Object[3];

        for (int[] move : allPossibleMoves) {
            int[][] dummyBoard = playNextMove(matrix, move, true);

            // If the white player has a winning score in that temporary board, return the move.
            if (getScore(dummyBoard, true, false) >= winScore) {
                losingMove[1] = move[0];
                losingMove[2] = move[1];
                return losingMove;
            }
        }

        return losingMove;
    }

    public Object[] minimaxSearchAB(int depth, int[][] board, boolean max, double alpha, double beta) {
        if (depth == 0) {
            Object[] x = {evaluateBoardForWhite(board, !max), null, null};
            return x;
        }

        ArrayList<int[]> allPossibleMoves = generateMoves(board);

        if (allPossibleMoves.size() == 0) {

            Object[] x = {evaluateBoardForWhite(board, !max), null, null};

            return x;
        }

        Object[] bestMove = new Object[3];

        if (max) {
            bestMove[0] = -1.0;

            for (int[] move : allPossibleMoves) {
                // Chơi thử với move hiện tại
                int[][] dummyBoard = playNextMove(board, move, false);

                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, !max, alpha, beta);

                // Cập nhật alpha
                if ((Double) (tempMove[0]) > alpha) {
                    alpha = (Double) (tempMove[0]);
                }
                // Cắt tỉa beta
                if ((Double) (tempMove[0]) >= beta) {
                    return tempMove;
                }
                if ((Double) tempMove[0] > (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }

        } else {
            bestMove[0] = 100000000.0;
            bestMove[1] = allPossibleMoves.get(0)[0];
            bestMove[2] = allPossibleMoves.get(0)[1];
            for (int[] move : allPossibleMoves) {
                int[][] dummyBoard = playNextMove(board, move, true);

                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, !max, alpha, beta);

                // Cập nhật beta
                if (((Double) tempMove[0]) < beta) {
                    beta = (Double) (tempMove[0]);
                }
                // Cắt tỉa alpha
                if ((Double) (tempMove[0]) <= alpha) {
                    return tempMove;
                }
                if ((Double) tempMove[0] < (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }
        return bestMove;
    }

    public double evaluateBoardForWhite(int[][] board, boolean userTurn) {

        double blackScore = getScore(board, true, userTurn);
        double whiteScore = getScore(board, false, userTurn);

        if (blackScore == 0) {
            blackScore = 1.0;
        }

        return whiteScore / blackScore;

    }

    public ArrayList<int[]> generateMoves(int[][] boardMatrix) {
        ArrayList<int[]> moveList = new ArrayList<int[]>();

        int boardSize = boardMatrix.length;

        // Tìm những tất cả những ô trống nhưng có đánh XO liền kề
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                if (boardMatrix[i][j] > 0) {
                    continue;
                }

                if (i > 0) {
                    if (j > 0) {
                        if (boardMatrix[i - 1][j - 1] > 0
                                || boardMatrix[i][j - 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (j < boardSize - 1) {
                        if (boardMatrix[i - 1][j + 1] > 0
                                || boardMatrix[i][j + 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (boardMatrix[i - 1][j] > 0) {
                        int[] move = {i, j};
                        moveList.add(move);
                        continue;
                    }
                }
                if (i < boardSize - 1) {
                    if (j > 0) {
                        if (boardMatrix[i + 1][j - 1] > 0
                                || boardMatrix[i][j - 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (j < boardSize - 1) {
                        if (boardMatrix[i + 1][j + 1] > 0
                                || boardMatrix[i][j + 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (boardMatrix[i + 1][j] > 0) {
                        int[] move = {i, j};
                        moveList.add(move);
                        continue;
                    }
                }

            }
        }
        return moveList;

    }

    // Đánh giá bàn cờ dựa trên tổng số điểm hàng ngan, hàng dọc, và 2 đường chéo
    public int getScore(int[][] board, boolean forX, boolean blacksTurn) {

        return evaluateHorizontal(board, forX, blacksTurn)
                + evaluateVertical(board, forX, blacksTurn)
                + evaluateDiagonal(board, forX, blacksTurn);
    }

    public static int evaluateHorizontal(int[][] boardMatrix, boolean forX, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[0].length; j++) {

                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    //2. Đếm...
                    consecutive++;
                } // gặp ô trống
                else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        // Ra: Ô trống ở cuối sau khi đếm. Giảm block rồi bắt đầu tính điểm sau đó reset lại ban đầu
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        // 1. Vào reset lại blocks = 1 rồi bắt đầu đếm
                        blocks = 1;
                    }
                } //gặp quân địch
                else if (consecutive > 0) {
                    // 2.Ra:  Ô bị chặn sau khi đếm. Tính điểm sau đó reset lại.
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    //1. Vào: reset lại blocks = 2 rồi bắt đầu đếm
                    blocks = 2;
                }
            }

            // 3. Ra: nhưng lúc này đang ở cuối. Nếu liên tục thì vẫn tính cho đến hết dòng
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            // reset lại để tiếp tục chạy cho dòng tiếp theo
            consecutive = 0;
            blocks = 2;

        }
        return score;
    }
    // hàm tính toán đường dọc tương tự như đường ngan

    public static int evaluateVertical(int[][] boardMatrix, boolean forX, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for (int j = 0; j < boardMatrix[0].length; j++) {
            for (int i = 0; i < boardMatrix.length; i++) {
                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }
            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            consecutive = 0;
            blocks = 2;

        }
        return score;
    }
    // Hàm tính toán 2 đường chéo tương tự như hàng ngan

    public static int evaluateDiagonal(int[][] boardMatrix, boolean forX, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        // Đường chéo /
        for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
            int iStart = Math.max(0, k - boardMatrix.length + 1);
            int iEnd = Math.min(boardMatrix.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;

                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }

            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        // Đường chéo \
        for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;

                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }

            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        return score;
    }

    public static int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
        final int winGuarantee = 1000000;
        if (blocks == 2 && count <= 5) {
            return 0;
        }
        switch (count) {
            // Ăn 5 -> Cho điểm cao nhất
            case 5: {
                return winScore;
            }
            case 4: {
                // Đang 4 -> Tuỳ theo lược và bị chặn: winGuarantee, winGuarantee/4, 200
                if (currentTurn) {
                    return winGuarantee;
                } else {
                    if (blocks == 0) {
                        return winGuarantee / 4;
                    } else {
                        return 200;
                    }
                }
            }
            case 3: {
                // Đang 3: Block = 0
                if (blocks == 0) {
                    // Nếu lược của currentTurn thì ăn 3 + 1 = 4 (không bị block) -> 50000 -> Khả năng thắng cao. 
                    // Ngược lại không phải lược của currentTurn thì khả năng bị blocks cao
                    if (currentTurn) {
                        return 50000;
                    } else {
                        return 200;
                    }
                } else {
                    // Block == 1 hoặc Blocks == 2
                    if (currentTurn) {
                        return 10;
                    } else {
                        return 5;
                    }
                }
            }
            case 2: {
                // Tương tự với 2
                if (blocks == 0) {
                    if (currentTurn) {
                        return 7;
                    } else {
                        return 5;
                    }
                } else {
                    return 3;
                }
            }
            case 1: {
                return 1;
            }
        }
        return winScore * 2;
    }

    public int[][] getMatrixBoard() {
        int matrix[][] = new int[row][col];
        for (int i = 0; i < Buttons.length; i++) {
            for (int j = 0; j < Buttons.length; j++) {
                int value = Buttons[i][j].value;
                matrix[i][j] = value;
            }
        }

        return matrix;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        compretitorTurnJLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        yourTurnJLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Bạn");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setForeground(new java.awt.Color(102, 102, 102));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Đối thủ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Chơi với máy");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Tỉ số:  0-0");

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        compretitorTurnJLabel.setForeground(new java.awt.Color(0, 0, 204));
        compretitorTurnJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        compretitorTurnJLabel.setText("Máy");

        jLabel3.setText("x/o");

        jLabel9.setText("Số ván thắng");

        jLabel5.setText("x/o");

        jLabel10.setText("Nickname");

        jPanel6.setBackground(new java.awt.Color(102, 102, 102));

        jLabel19.setBackground(new java.awt.Color(102, 102, 102));

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jLabel11.setText("Số ván chơi");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel12.setText("{nickname}");

        jLabel13.setText("{sovanchoi}");

        jLabel14.setText("{sovanthang}");

        jLabel15.setText("Máy");

        jLabel16.setText("Nhiều lắm");

        jLabel2.setText("Số ván thắng");

        yourTurnJLabel3.setForeground(new java.awt.Color(255, 0, 0));
        yourTurnJLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        yourTurnJLabel3.setText("Bạn");

        jLabel8.setText("Nickname");

        jLabel4.setText("Số ván chơi");

        jLabel17.setText("Nhiều lắm");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(yourTurnJLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(159, 159, 159)
                                .addComponent(compretitorTurnJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel13))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel14))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel15))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel16))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel17))))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(compretitorTurnJLabel)
                            .addComponent(yourTurnJLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        //for(int i=0; i<5; i++){
            //    for(int j=0;j<5;j++){
                //        jPanel1.add(button[i][j]);
                //    }
            //}

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameAIFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameAIFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameAIFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameAIFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameAIFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel compretitorTurnJLabel;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel yourTurnJLabel3;
    // End of variables declaration//GEN-END:variables
}