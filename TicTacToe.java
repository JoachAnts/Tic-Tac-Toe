import java.util.*;

class TicTacToe
{
	int num_nodes = 0;
	
	private final int EMPTY = 0;
	private final int NOUGHT = 1;
	private final int CROSS = 2;
	private final int PLAYER = NOUGHT;
	private final int COMPUTER = CROSS;
	
	private boolean gameOver;
	private int[] gameBoard;
	private int comNextMove;
	
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		TicTacToe game = new TicTacToe();
		
		//game.testEmptySquares();
		
		while (!game.gameOver())
		{
			System.out.print("Choose a square (0-8): ");
			int choice = scanner.nextInt();
			while (choice < 0 && choice > 8)
			{
				System.out.print("Choose a square (0-8): ");
				choice = scanner.nextInt();
			}
			game.movePlayer(choice);
			game.drawBoard();
			game.checkGameState();
			if (game.gameOver())
				break;
			
			game.moveComputer();
			game.drawBoard();
			game.checkGameState();
			if (game.gameOver())
				break;
		}
	}
	
	public TicTacToe()
	{
		gameOver = false;
		comNextMove = 0;
		gameBoard = new int[9];
		Arrays.fill(gameBoard, 0);
	}
	
	public boolean gameOver() { return gameOver; }
	
	public void testEvaluateBoard()
	{
		int[] board = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		System.out.println("Evaluation of empty board: "+evaluateBoard(board));
		board[0] = NOUGHT; board[1] = NOUGHT; board[2] = NOUGHT;
		System.out.println("Evaluation of NOUGHT win board: "+evaluateBoard(board));
		board[0] = CROSS; board[1] = CROSS; board[2] = CROSS;
		System.out.println("Evaluation of CROSS win board: "+evaluateBoard(board));
	}
	
	public void testEmptySquares()
	{
		System.out.println("Testing empty squares function");
		int[][] boards = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1},
			{2, 2, 2, 2, 2, 2, 2, 2, 2},
			{1, 1, 1, 1, 1, 1, 1, 1, 0},
			{1, 1, 1, 1, 1, 1, 1, 0, 1},
			{1, 1, 1, 1, 1, 1, 0, 1, 1},
			{1, 1, 1, 1, 1, 0, 1, 1, 1},
			{1, 1, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 1, 0, 1, 1, 1, 1, 1},
			{1, 1, 0, 1, 1, 1, 1, 1, 1},
			{1, 0, 1, 1, 1, 1, 1, 1, 1},
			{0, 1, 1, 1, 1, 1, 1, 1, 1},
		};
		for (int j = 0; j < boards.length; j++)
		{
			ArrayList<Integer> indices = getEmptySquares(boards[j]);
			for (int i = 0; i < indices.size(); i++)
			{
				System.out.print(indices.get(i));
			}
			System.out.println();
		}
	}
	
	public int evaluateBoard(int[] board)
	{			
		// Each vector represents a 3 in a row vector on the board that needs checking
		int[][] checkVectors = new int[][] {
			{0, 1, 2},
			{3, 4, 5},
			{6, 7, 8},
			{0, 3, 6},
			{1, 4, 7},
			{2, 5, 8},
			{0, 4, 8},
			{2, 4, 6}
		};
		
		for (int v = 0; v < checkVectors.length; v++)
		{
			if (board[checkVectors[v][0]] == board[checkVectors[v][1]] && board[checkVectors[v][1]] == board[checkVectors[v][2]])
			{
				if (board[checkVectors[v][0]] == NOUGHT)
					return -10;
				else if (board[checkVectors[v][0]] == CROSS)
					return 10;
			}
		}
		
		return 0;
	}
	
	public boolean isDraw(int[] board)
	{
		for (int i = 0; i < board.length; i++)
		{
			if (board[i] == EMPTY)
				return false;
		}
		return true;
	}
	
	public ArrayList<Integer> getEmptySquares(int[] board)
	{
		ArrayList<Integer> emptySquares = new ArrayList<>();
		for (int i = 0; i < board.length; i++)
		{
			if (board[i] == EMPTY)
				emptySquares.add(i);
		}
		return emptySquares;
	}
	
	public void moveComputer()
	{
		System.out.println("Best score found: "+minimax(gameBoard, COMPUTER, 0, 10));
		System.out.println("Number of nodes in game tree: "+num_nodes);
		System.out.println("Square: "+comNextMove);
		gameBoard[comNextMove] = COMPUTER;
	}
	
	public int minimax(int[] board, int turn, int depth, int maxDepth)
	{
		if (depth == 0)
			num_nodes = 0;
		else
			++num_nodes;
		
		int score = evaluateBoard(board);
		if (depth == maxDepth)
			System.out.println("Reached depth limit");
		if (depth == maxDepth || score == 10 || score == -10 || isDraw(board))
			return score;
		
		ArrayList<Integer> emptySquares = getEmptySquares(board);
		int bestScore = 0;
		if (turn == CROSS)
		{
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < emptySquares.size(); i++)
			{
				int[] boardCopy = Arrays.copyOf(board, board.length);
				boardCopy[emptySquares.get(i)] = turn;
				int nextScore = minimax(boardCopy, NOUGHT, depth+1, maxDepth);
				if (nextScore > max)
				{
					max = nextScore;
					if (depth == 0)
						comNextMove = emptySquares.get(i);
				}
			}
			bestScore = max;
		}
		else if (turn == NOUGHT)
		{
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < emptySquares.size(); i++)
			{
				int[] boardCopy = Arrays.copyOf(board, board.length);
				boardCopy[emptySquares.get(i)] = turn;
				int nextScore = minimax(boardCopy, CROSS, depth+1, maxDepth);
				if (nextScore < min)
				{
					min = nextScore;
					//comNextMove = emptySquares.get(i);
				}
			}
			bestScore = min;
		}
		else
		{
			System.err.println("This error should not be printed");
			System.exit(1);
		}
		return bestScore;
	}
	
	public void movePlayer(int num)
	{
		// num must be in range [0, 8]
		if (num < 0 || num > 8 || gameBoard[num] != EMPTY)
		{
			System.err.println("Error: Invalid number, must be between 0 and 8 and refer to empty square");
			System.exit(1);
		}
		gameBoard[num] = PLAYER;
	}
	
	public void checkGameState()
	{
		int boardScore = evaluateBoard(gameBoard);
		switch (boardScore)
		{
			case 10:
				System.out.println("Cross wins!");
				gameOver = true;
				break;
			case -10:
				System.out.println("Nought wins!");
				gameOver = true;
				break;
		}
		if (isDraw(gameBoard))
		{
			System.out.println("It's a draw!");
			gameOver = true;
		}
	}
	
	public void drawBoard()
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				switch (gameBoard[i*3+j])
				{
					case EMPTY:
						System.out.print("-");
						break;
					case NOUGHT:
						System.out.print("O");
						break;
					case CROSS:
						System.out.print("X");
						break;
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}