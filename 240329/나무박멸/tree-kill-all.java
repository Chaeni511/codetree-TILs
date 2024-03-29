import java.util.*;
import java.io.*;

public class Main {
    static int answer = 0;

    static int n;
    static int m;
    static int k;
    static int c;
    static int[][] land;
    static int[][] herbicide;

    static int[] ti= {-1, 0, 1, 0};
    static int[] tj= {0, -1, 0, 1};
    static int[] hi= {-1, -1, 1, 1};
    static int[] hj= {-1, 1, 1, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st0 = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st0.nextToken());
        m = Integer.parseInt(st0.nextToken());
        k = Integer.parseInt(st0.nextToken());
        c = Integer.parseInt(st0.nextToken());

        land = new int[n][n];
        herbicide = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            StringTokenizer st1 = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                land[i][j] = Integer.parseInt(st1.nextToken());
            }
        }

        for(int mm = 0; mm < m; mm++) {
            herbicideResistance();
            land = herbicide(grow());
        }
        System.out.println(answer);
    }

    public static int[][] grow() {
        int[][] tmp = new int[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                // 나무가 있는 곳 일 때
                if(land[i][j] > 0) {
                    
                    int emptyCnt = 0;
                    int treeCnt = 0;
                    Queue<int[]> toGrow = new LinkedList<int[]>();

                    for (int d = 0; d < 4; d++) {
                        int ni = i + ti[d];
                        int nj = j + tj[d];

                        if (
                            0 <= ni && 0 <= nj && ni < n && nj < n
                            && herbicide[ni][nj]<= 0
                        ){
                            if (land[ni][nj] == 0) {
                                emptyCnt++;
                                int[] tmpToGrow = {ni, nj};
                                toGrow.add(tmpToGrow);
                            } else if (land[ni][nj] > 0) {
                                treeCnt++;
                            }
                            
                        }
                    }

                    tmp[i][j] = land[i][j] + treeCnt;

                    while (!toGrow.isEmpty()) {
                        int[] next = toGrow.poll();
                        tmp[next[0]][next[1]] += tmp[i][j] / emptyCnt;
                    }
                    
                } else if (land[i][j] == -1) {
                    tmp[i][j] = -1;
                }

            }
        }
        // System.out.println("tmp =========================");
        // for (int z= 0; z < n; z++) {
        //     System.out.println(Arrays.toString(tmp[z]));
        // }
        return tmp;

    }

    public static int[][] herbicide(int[][] tmpLand) {
        int maxKillCnt = -1;
        int[] maxKillLocation = {0, 0};

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if (tmpLand[i][j] <=0) {continue;}

                int killCnt = tmpLand[i][j];
                for (int h = 1; h <= k; h++) {
                    for (int d = 0; d < 4; d++) {
                        int ni = i + h * hi[d];
                        int nj = j + h * hj[d];

                        if (
                            0 <= ni && 0 <= nj && ni < n && nj < n
                            && tmpLand[ni][nj] > 0
                            && herbicide[ni][nj] >= 0
                        ) {
                            killCnt += tmpLand[ni][nj];
                        }
                        
                    }
                }
                // 최대값인지 확인
                if (killCnt > maxKillCnt) {
                    maxKillCnt = killCnt;
                    maxKillLocation[0] = i;
                    maxKillLocation[1] = j;
                }
            }
        }

        // 제초제 뿌릴 곳 확정
        answer += maxKillCnt;
        herbicide[maxKillLocation[0]][maxKillLocation[1]] = c;
        for (int h = 1; h <= k; h++) {
            for (int d = 0; d < 4; d++) {
                int ni = maxKillLocation[0] + h * hi[d];
                int nj = maxKillLocation[1] + h * hj[d];
                if (0 <= ni && 0 <= nj && ni < n && nj < n) {
                    herbicide[ni][nj] = c;
                    tmpLand[ni][nj] = 0;
                }
            }
        }

        // System.out.println("maxKillCnt: "+ Integer.toString(maxKillCnt));
        // System.out.println("maxKillLocation: "+ Integer.toString(maxKillLocation[0]) + ", " + Integer.toString(maxKillLocation[1]));
        // System.out.println("herbicide =========================");
        // for (int z= 0; z < n; z++) {
        //     System.out.println(Arrays.toString(herbicide[z]));
        // }
        return tmpLand;
    }

    public static void herbicideResistance() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (herbicide[i][j] > 0) {
                    herbicide[i][j] -= 1;
                }
            }
        }
    }
}

// 5 2 2 1
// 0 0 0 0 0
// 0 30 23 0 0
// 0 0 -1 0 0
// 0 0 17 46 77
// 0 0 0 12 0