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
            land = grow();

            herbicideResistance();

            herbicide();
        }

        System.out.println(answer);
    }


    // 나무의 성장과 번식
    public static int[][] grow() {
        int[][] tmp = new int[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                // 나무가 있는 곳 일 때
                if(land[i][j] > 0) {
                    
                    int emptyCnt = 0;
                    int treeCnt = 0;
                    Queue<int[]> toGrow = new LinkedList<int[]>();
                    // 델타 탐색하면서
                    for (int d = 0; d < 4; d++) {
                        int ni = i + ti[d];
                        int nj = j + tj[d];

                        // 범위 안인데
                        if (0 <= ni && 0 <= nj && ni < n && nj < n){
                            // 빈땅이면 나무가 번식
                            if (land[ni][nj] == 0 && herbicide[ni][nj] == 0) {
                                emptyCnt++;
                                int[] tmpToGrow = {ni, nj};
                                toGrow.add(tmpToGrow);
                            // 나무가 있으면 나무가 성장
                            } else if (land[ni][nj] > 0) {
                                treeCnt++;
                            }
                            
                        }
                    }
                   
                    // 나무 성장
                    tmp[i][j] = land[i][j] + treeCnt;

                    // 나무 번식
                    while (!toGrow.isEmpty()) {
                        int[] next = toGrow.poll();
                        tmp[next[0]][next[1]] += tmp[i][j] / emptyCnt;
                    }

                // 벽 표시
                } else if (land[i][j] == -1) {
                    tmp[i][j] = -1;
                }

            }
        }
      
        return tmp;
    }

    public static void herbicide() {
        int maxKillCnt = -1;
        int[] maxKillLocation = {0, 0};

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                // 나무가 없으면 넘어감
                if (land[i][j] <= 0) continue;

                int killCnt = land[i][j];
                // 대각선 탐색
                for (int d = 0; d < 4; d++) {
                    int ni = i;
                    int nj = j;
                    
                    for (int h = 1; h <= k; h++) {
                        ni += hi[d];
                        nj += hj[d];
                    
                        // 범위 밖이면 브레이크
                        if ( 0 > ni || 0 > nj || ni >= n || nj >= n ) break;
                        // 나무가 없거나 벽이 있으면 브레이크
                        if (land[ni][nj] <= 0) break;
                        
                        killCnt += land[ni][nj];
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

        if (land[maxKillLocation[0]][maxKillLocation[1]] > 0) {
            herbicide[maxKillLocation[0]][maxKillLocation[1]] = c;
            land[maxKillLocation[0]][maxKillLocation[1]] = 0;
        
            for (int d = 0; d < 4; d++) {
                int ni = maxKillLocation[0];
                int nj = maxKillLocation[1];
                for (int h = 1; h <= k; h++) {
                    ni += hi[d];
                    nj += hj[d];

                    // 범위 밖이면 브레이크
                    if ( 0 > ni || 0 > nj || ni >= n || nj >= n ) break;
                    // 나무가 없거나 벽이 있으면 브레이크
                    if (land[ni][nj] <= 0) break;

                    if(land[ni][nj] == 0){
                        herbicide[ni][nj] = c;
                        break;
                    }
                    herbicide[ni][nj] = c;
                    land[ni][nj] = 0;
                }
            }
        }      
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