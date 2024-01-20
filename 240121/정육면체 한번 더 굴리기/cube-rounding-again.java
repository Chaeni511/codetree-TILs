import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.*;


public class Main {
    static int n;
    static int m;

    static int[][] board;
    static int[] dice = {4, 2, 3, 5, 6, 1};

    // 주사위 이동 방향 0동 1남 2서 3북
    static int[] dr = {0, 1, 0, -1};
    static int[] dc = {1, 0, -1, 0};

    // 뱡향
    static int dir = 0;

    // 반시계 방향 -1 시계 방향 1
    static int dirTo = 0;
    // 큐
    static Queue<int[]> q = new ArrayDeque<int[]>();

    public static void main(String[] args) throws Exception {

        // 입력
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n][n];

        for(int i = 0; i < n; i++) {
            String tmp = br.readLine();
            StringTokenizer tmpSt = new StringTokenizer(tmp);

            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(tmpSt.nextToken());
            }
        }

        // 시작
        int answer = 0;
        int row = 0;
        int col = 1;

        for (int tc = 0; tc < m; tc++) {
            // 격자판을 벗어났다면
            // if (
            //     row + dr[dir] < 0 || row + dr[dir] > n 
            //     || col + dc[dir] < 0 || col + dc[dir] > 0
            // ) {
            //     dir = (dir + 2) % 4;
            //     // System.out.println("여기 오나??");
            // }
            
            // 점수 얻기
            answer += bfs(row, col, 0);

            // 진행방향 전환
            dir = (dir + changeDir(row, col)) % 4;
            if (dir < 0) {
                dir += 4;
            }

            // 해당 방향으로 진행 가능한지 확인
            while(!checkDir(row, col, dir)) {
                dir = (dir + 2) % 4;
            }
            
            // 주사위 굴리기
            roll(dir);
            
            row = (row + dr[dir]) % 4;
            col = (col + dc[dir]) % 4;

            if (row < 0) {
                row += n;
            }
            if (col < 0) {
                col += n;
            }
        } 

        System.out.println(answer);
    }


    // 점수 얻기
    public static Integer bfs (int i, int j, int res) {
        q.add(new int[] {i, j});
        int num = board[i][j];

        int[][] visited = new int[n][n];
        
        while (!q.isEmpty()) {
            int[]tmp = q.poll();
            int r = tmp[0];
            int c = tmp[1];

            if (board[r][c] != num) {
                continue;
            }

            res += num;
            visited[r][c] = 1;

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr >= 0 && nr < n && nc >= 0 && nc < n && visited[nr][nc] == 0) {
                    q.add(new int[] {nr, nc});
                }
            }
        }
        return res;
    }

    // 방향 전환
    public static int changeDir(int i, int j) {
        int bottom = Math.abs(7 - dice[0]);
       
        // 주사위의 아랫면이 보드의 해당 칸에 있는 숫자보다 크면 90' 시계방향
        if (bottom > board[i][j]) {
            dirTo = -1;
            return 1;
        // 더 작다면 현재 진행방향에서 90' 반시계방향
        } else if (bottom < board[i][j]) {
            dirTo = 1;
            return -1;
        }
        return 0;
    }

    // 방향 전환 후 진행 가는 여부 확인
    public static Boolean checkDir(int i, int j, int d) {
        int ni = i + dr[d];
        int nj = j + dc[d];

        if (ni < 0 || nj < 0 || ni > n || nj > n) {
            dir += dirTo;
            return false;
        }
        return true;
    }

    // 주사위 굴리기
    public static void roll(int d) {
        int tmp = dice[0];
        
        // 동
        if (d == 0) {
            dice[0] = dice[4];
            dice[4] = dice[2];
            dice[2] = dice[5];
            dice[5] = tmp;

        // 남
        } else if (d == 1) {
            dice[0] = dice[3];
            dice[3] = dice[2];
            dice[2] = dice[1];
            dice[1] = tmp;
            
        // 서
        } else if (d == 2) {
            dice[0] = dice[5];
            dice[5] = dice[2];
            dice[2] = dice[4];
            dice[4] = tmp;

        // 북
        } else {
            dice[0] = dice[1];
            dice[1] = dice[2];
            dice[2] = dice[3];
            dice[3] = tmp;
        }
    }
}