import java.util.*;
import java.io.*;

public class Main {
    static int M;
    static int T;
    static Queue<Integer>[][] board = new ArrayDeque[5][5];
    static int[][] eggs = new int[5][5];
    // 팩맨 위치
    static int[] packman = new int[2];
    // 방향 1-8
    static int[] di = {0, -1, -1, 0, 1, 1, 1, 0, -1}
    static int[] dj = {0, 0, -1, -1, -1, 0, 1, 1, 1}
    // 몬스터 이동용
    static Queue<Integer>[][] tmpBoard;
    // 몬스터 죽인 수 카운트 용
    static int maxKill = 0;
    int[][] maxKillRoute = new int[3][2];


    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                board[i][j] = new ArrayDeque<Integer>();
            }
        }

        // 팩맨 위치 받기
        StringTokenizer st2 = new StringTokenizer(br.readLine());
        packman[0] = Integer.parseInt(st2.nextToken());
        packman[1] = Integer.parseInt(st2.nextToken());

        // 몬스터 위치 받기
        for(int m = 0; m < M; m++) {
            StringTokenizer st3 = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st3.nextToken());
            int c = Integer.parseInt(st3.nextToken());
            int d = Integer.parseInt(st3.nextToken());
            board[r][c].offer(d);
            // 몬스터 복제 시도용
            eggs[r][c] = d;
        }

        for (int = 0; t < T; t++) {
            // 몬스터 이동
            tmpBoard = moveMonsters();
            // 팩맨 이동
            movePackman();
            // 몬스터 시체 소멸
            // 몬스터 복제 완성

        }
        
    }

    public static ArrayDeque[][] moveMonsters() {
        Queue<Integer>[][] tmpBoard = new ArrayDeque[5][5];
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                tmpBoard[i][j] = new ArrayDeque<Integer>();
                while(!board[r][c].isEmpty()) {
                    int d = board[r][c].pop();
                    int ni = i + di[d];
                    int nj = j + dj[d];
                    int cnt = 1
                    while (ni < 1 || nj < 1 || ni > 5 || nj > 5 || board[ni][nj] < 0 || cnt <= 8) {
                        d = (d+1) % 9;
                        ni = i + di[d];
                        nj = j + dj[d];
                        cnt++;
                    }
                    if(cnt == 8) {
                        break;
                    }
                    tmpBoard[i][j].add(d);

                }
            }
        }
        return tmpBoard;
    }

    // 상좌하우 1 3 5 7
    public static int[][] movePackman(){
        int i = packman[0];
        int j = packman[1];
        dfs(i, j, 0, 0, new int[3][2]);
        for(int k = 0; k < 3; k++) {
            int ri = killRoute[k][0];
            int rj = killRoute[k][1];
            while(tmpBoard[ri][rj]){
                
            }
        }
    }
    public static void dfs(i, j, cnt, kill, killRoute) {
        if (cnt == 2 && kill > maxKill) {
            maxKill = kill;
            maxKillRoute = killRoute
        }
        for(int d = 1; d <= 7; d+2) {
            int ni = i + di[d];
            int nj = j + dj[d];
            killRoute[cnt][0] = ni;
            killRoute[cnt][1] = nj;
            dfs(ni, nj, cnt+1, kill + tmpBoard[ni][nj].size(), killRoute);
        }
    }
}