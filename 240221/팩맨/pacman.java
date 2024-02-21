import java.util.*;
import java.io.*;

public class Main {
    static int M;
    static int T;
    // 몬스터 위치 (2차원 배열에 큐를 넣음)
    static Deque<Integer>[][] board = new Deque[4][4];

    // 팩맨 위치
    static int[] packman = new int[2];
    // 알 위치
    static Deque<Integer>[][] eggs;

    // 이동 방향
    static int[] di = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dj = {0, -1, -1, -1, 0, 1, 1, 1};

    // 팩맨이 죽인 수 확인용
    static int maxKill = 0;
    static int[][] maxKillRoutes;

    public static void main(String[] args) throws Exception {
        int answer = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        

        // 팩맨 위치 받기
        StringTokenizer st2 = new StringTokenizer(br.readLine());
        packman[0] = Integer.parseInt(st2.nextToken()) - 1;
        packman[1] = Integer.parseInt(st2.nextToken()) - 1;

        // board 초기화
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new ArrayDeque<Integer>();
            }
        }


        // 몬스터 위치 받기
        for (int i = 0; i < M; i++) {
            StringTokenizer st3 = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st3.nextToken()) - 1;
            int c = Integer.parseInt(st3.nextToken()) - 1;
            int d = Integer.parseInt(st3.nextToken()) - 1;

            board[r][c].add(d);
        }

        // 시작
        for (int t = 0; t < T; t++) {
            System.out.println(Integer.toString(t) + "================");
            // 몬스터 복제 시도
            eggs = board.clone();

            System.out.println("몬스터 복제 시도");
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(eggs[k]));
            }
            // 몬스터 이동
            board = moveMonsters();
            
            System.out.println("몬스터 이동");
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(board[k]));
            }
            // 팩맨 이동
            movePackman();   

            System.out.println("팩맨 이동");
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(board[k]));
            }
            System.out.println(Integer.toString(maxKill));
            for (int k = 0; k < 3; k++) {
                System.out.println(Arrays.toString(maxKillRoutes[k]));
            }

            // 몬스터 시체 소멸
            checkDeadMonsters();
            System.out.println("몬스터 시체 소멸");
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(board[k]));
            }

            // 몬스터 복제 완성
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(eggs[k]));
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j< 4; j++) {
                    if (!eggs[i][j].isEmpty() && eggs[i][j].peek() > 0) {
                        while(!eggs[i][j].isEmpty()) {
                            board[i][j].add(eggs[i][j].poll());
                        }
                        
                    }
                }
            }
            System.out.println("몬스터 복제 완성");
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(board[k]));
            }
            for (int k = 0; k < 4; k++) {
                System.out.println(Arrays.deepToString(eggs[k]));
            }

        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j< 4; j++) {
           
                System.out.println(!board[i][j].isEmpty() && board[i][j].peek() > 0);
                if (!eggs[i][j].isEmpty() && board[i][j].peek() > 0) {
                    answer += board[i][j].size();
                }
            }
        }
        System.out.println("answer=====");
        System.out.println(answer);
    }

    public static void checkDeadMonsters() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!board[i][j].isEmpty()) {
                    int dead = board[i][j].peek();
                    int size = board[i][j].size();
                    if (dead == -3) {
                        for (int s = 0; s < size; s++) {
                            board[i][j].poll();
                        }
                    } else if (dead < 0) {
                        for (int s = 0; s < size; s++) {
                            board[i][j].add(board[i][j].poll() - 1);
                        }
                    }
                }
            }
        }
    }

    public static Deque<Integer>[][] moveMonsters() {
        Deque<Integer>[][] tmp = new Deque[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = new ArrayDeque<Integer>();
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                while (!board[i][j].isEmpty()) {
                    boolean flag = true;
                    int originalD = board[i][j].poll();
                    for (int d = 0; d < 8; d++){
                        int dir = (originalD + d) % 8;
                        int ni = i + di[dir];
                        int nj = j + dj[dir];
                        // 팩맨이 없고 격자 안인데
                        if (!(ni == packman[0] && nj == packman[1]) 
                            && 0 <= ni && ni < 4
                            && 0 <= nj && nj < 4 
                        ) {
                            // 아무도 없는 곳이거나 시체가 아닌 다른 몬스터가 있는 곳 일 때
                            if ((!board[ni][nj].isEmpty() && board[ni][nj].peek() > 0) || board[ni][nj].isEmpty()) {
                                tmp[ni][nj].add(dir);
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        tmp[i][j].add(originalD);
                    }
                }
            }
        }
        return tmp;
    }

    public static void movePackman() {
        dfs(packman[0], packman[1], 0, 0, new int[3][2]);
        // System.out.println()
        for (int[] maxKillRoute: maxKillRoutes) {
            int i = maxKillRoute[0];
            int j = maxKillRoute[1];
            int size = board[i][j].size();
            for (int s = 0; s < size; s++) {
                board[i][j].poll();
                board[i][j].add(-1);
            }
        }
    }


    public static void dfs(int x, int y, int c, int kill, int[][] routes) {
        if (c == 3) {
            if (kill > maxKill) {
                maxKill = kill;
                maxKillRoutes = routes;
            }
            return;
        }
        for (int d = 0; d < 8; d += 2) {
            int nx = x + di[d];
            int ny = y + dj[d];
            if (0 <= nx && nx < 4 && 0 <= ny && ny < 4) {
                routes[c][0] = nx;
                routes[c][1] = ny;
                if (board[nx][ny].size() > 0 && board[nx][ny].peek() > 0) {
                    dfs(nx, ny, c+1, kill + board[nx][ny].size(), routes); 
                } else {
                    dfs(nx, ny, c+1, kill, routes);
                }
            }
        }
    }
}