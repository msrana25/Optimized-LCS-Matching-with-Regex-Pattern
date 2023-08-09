import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class PatternMatching {
	public static List<String> dictionaryInputMatcherWithPattern(PriorityQueue<String> pq, String pattern) {
		int validWordCount = 0;
		List<String> wordList = new ArrayList<>();
		while (pq.size() > 0 && validWordCount < 3) {
			String rem = pq.poll();
			boolean isStringMatching = patternMatcher(rem, pattern);
			if (isStringMatching) {
				validWordCount++;
				wordList.add(rem);
			}
		}

		return wordList;

	}

	public static int findTwoStringsLCS(String s1, String s2) {
		int m = s1.length();
		int n = s2.length();
		int[][] dp = new int[m + 1][n + 1];

		for (int i = 1; i < dp.length; i++) {
			for (int j = 1; j < dp[0].length; j++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					dp[i][j] = 1 + dp[i - 1][j - 1];
				} else {
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
		printLCS2D(dp, m, n, s1, s2, "");
		return dp[m][n];
	}

	public static int findThreeStringLCS(String s1, String s2, String s3) {
		int m = s1.length();
		int n = s2.length();
		int p = s3.length();

		int[][][] dp = new int[m + 1][n + 1][p + 1];

		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				for (int k = 0; k <= p; k++) {
					if (i == 0 || j == 0 || k == 0) {
						dp[i][j][k] = 0;
					} else if (s1.charAt(i - 1) == s2.charAt(j - 1) && s2.charAt(j - 1) == s3.charAt(k - 1)) {
						dp[i][j][k] = dp[i - 1][j - 1][k - 1] + 1;
					} else {
						dp[i][j][k] = Math.max(Math.max(dp[i - 1][j][k], dp[i][j - 1][k]), dp[i][j][k - 1]);
					}
				}
			}
		}
		printLCS3D(dp, m, n, p, s1, s2, s3, "");
		return dp[m][n][p];
	}

	public static void printLCS2D(int[][] dp, int i, int j, String s1, String s2, String result) {

		if (i == 0 || j == 0) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream("output.txt"));
				pw.write(result);
				pw.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println(result);
			return;
		}

		int vertical = dp[i - 1][j];
		int horizontal = dp[i][j - 1];

		if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
			result = s1.charAt(i - 1) + result;
			printLCS2D(dp, i - 1, j - 1, s1, s2, result);
		} else if (vertical >= horizontal) {
			printLCS2D(dp, i - 1, j, s1, s2, result);
		} else {
			printLCS2D(dp, i, j - 1, s1, s2, result);
		}

		// abcbdab
		// bdcaba
	}

	public static void printLCS3D(int[][][] dp, int i, int j, int k, String s1, String s2, String s3, String result) {
		if (i == 0 || j == 0 || k == 0) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream("output.txt"));
				pw.write(result);
				pw.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println(result);
			return;
		}

		int vertical = dp[i - 1][j][k];
		int horizontal = dp[i][j - 1][k];
		int depth = dp[i][j][k - 1];

		if (s1.charAt(i - 1) == s2.charAt(j - 1) && s2.charAt(j - 1) == s3.charAt(k - 1)) {
			result = s1.charAt(i - 1) + result;
			printLCS3D(dp, i - 1, j - 1, k - 1, s1, s2, s3, result);
		} else if (vertical >= horizontal && vertical >= depth) {
			printLCS3D(dp, i - 1, j, k, s1, s2, s3, result);
		} else if (horizontal >= depth) {
			printLCS3D(dp, i, j - 1, k, s1, s2, s3, result);
		} else {
			printLCS3D(dp, i, j, k - 1, s1, s2, s3, result);
		}
	}

	public static boolean patternMatcher(String s, String p) {
		s = s.toLowerCase();
		p = p.toLowerCase();
		boolean[][] dp = new boolean[p.length() + 1][s.length() + 1];
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[0].length; j++) {
				if (i == 0 && j == 0) {
					dp[i][j] = true;
				} else if (i == 0) {
					dp[i][j] = false;

				} else if (j == 0) {
					char ch = p.charAt(i - 1);
					if (ch == '*') {
						dp[i][j] = dp[i - 2][j];

					} else {
						dp[i][j] = false;
					}
				} else {
					char sc = s.charAt(j - 1);
					char pc = p.charAt(i - 1);

					if (sc == pc) {
						dp[i][j] = dp[i - 1][j - 1];
					}

					else if (pc == '.') {
						dp[i][j] = dp[i - 1][j - 1];
					}

					else if (pc == '*') {
						char pslc = p.charAt(i - 2);

						dp[i][j] = dp[i - 2][j];

						if (pslc == sc || pslc == '.') {
							dp[i][j] = dp[i][j] || dp[i][j - 1];
						}
					} else {
						dp[i][j] = false;
					}

				}
			}
		}
		return dp[p.length()][s.length()];

	}

	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new FileInputStream("test.txt"));
			int n = Integer.valueOf(sc.nextLine());
			PriorityQueue<String> pq = new PriorityQueue<>();
			for (int i = 0; i < n; i++) {
				String word = sc.nextLine().toLowerCase();
				pq.add(word);
			}
			String pattern = sc.nextLine().toLowerCase();
			List<String> matchingWordList = dictionaryInputMatcherWithPattern(pq, pattern);
			int totalMatchingWords = matchingWordList.size();
			System.out.println(matchingWordList);
			if (totalMatchingWords < 2) {
				System.out.println("LCS not possible");
			}

			else if (totalMatchingWords == 2) {
				String s1 = matchingWordList.get(0);
				String s2 = matchingWordList.get(1);
				System.out.println(findTwoStringsLCS(s1, s2));
			}

			else if (totalMatchingWords == 3) {
				String s1 = matchingWordList.get(0);
				String s2 = matchingWordList.get(1);
				String s3 = matchingWordList.get(2);

				System.out.println(findThreeStringLCS(s1, s2, s3));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		sc.close();

	}

}
