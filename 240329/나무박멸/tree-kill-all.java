import java.util.Scanner;

public class Main {
    public static final int DIR_NUM = 4;
    public static final int MAX_N = 20;
    
    public static int n, m, k, c;
    public static int[][] tree = new int[MAX_N + 1][MAX_N + 1];
    public static int[][] addTree = new int[MAX_N + 1][MAX_N + 1];
    public static int[][] herb = new int[MAX_N + 1][MAX_N + 1];
    
    public static int ans;
    
    public static boolean isOutRange(int x, int y) {
        return !(1 <= x && x <= n && 1 <= y && y <= n);
    }
    
    // 입력을 받는 등 초기 작업을 합니다.
    public static void init() {
        Scanner sc = new Scanner(System.in);
        
        n = sc.nextInt();
        m = sc.nextInt();
        k = sc.nextInt();
        c = sc.nextInt();
        
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++)
                tree[i][j] = sc.nextInt();
    }
    
    // 1단계 : 인접한 네 개의 칸 중 나무가 있는 칸의 수만큼 나무가 성장합니다.
    public static void stepOne() {
        int[] dx = new int[]{-1,  0, 1, 0};
        int[] dy = new int[]{ 0, -1, 0, 1};
    
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++) {
                if(tree[i][j] <= 0) continue;
    
                // 나무가 있는 칸의 수(cnt)만큼 나무가 성장합니다.
                int cnt = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nx = i + dx[dir];
                    int ny = j + dy[dir];
                    if(isOutRange(nx, ny)) continue;
                    if(tree[nx][ny] > 0) cnt++;
                }
                tree[i][j] += cnt;
            }
    }
    
    // 2단계 : 기존에 있었던 나무들은 아무것도 없는 칸에 번식을 진행합니다.
    public static void stepTwo() {
        int[] dx = new int[]{-1,  0, 1, 0};
        int[] dy = new int[]{ 0, -1, 0, 1};
    
        // 모든 나무에서 동시에 일어나는 것을 구현하기 위해 하나의 배열을 더 이용합니다.
        // addTree를 초기화해줍니다.
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++) 
                addTree[i][j] = 0;
    
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++) {
                if(tree[i][j] <= 0) continue;
    
                // 해당 나무와 인접한 나무 중 아무도 없는 칸의 개수를 찾습니다.
                int cnt = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nx = i + dx[dir];
                    int ny = j + dy[dir];
                    if(isOutRange(nx, ny)) continue;
                    if(herb[nx][ny] > 0) continue;
                    if(tree[nx][ny] == 0) cnt++;
                }
    
                // 인접한 나무 중 아무도 없는 칸은 cnt로 나눠준 만큼 번식합니다.
                for(int dir = 0; dir < 4; dir++) {
                    int nx = i + dx[dir];
                    int ny = j + dy[dir];
                    if(isOutRange(nx, ny)) continue;
                    if(herb[nx][ny] > 0) continue;
                    if(tree[nx][ny] == 0) addTree[nx][ny] += tree[i][j] / cnt;
                }
            }
        
        // addTree를 더해 번식을 동시에 진행시킵니다.
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++) tree[i][j] += addTree[i][j];
    }
    
    // 3단계 : 가장 많이 박멸되는 칸에 제초제를 뿌립니다.
    public static void stepThree() {
        int[] dx = new int[]{-1,  1, 1, -1};
        int[] dy = new int[]{-1, -1, 1,  1};
    
        int maxDel = 0;
        int maxX = 1;
        int maxY = 1;
    
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++) {
                // 모든 칸에 대해 제초제를 뿌려봅니다. 각 칸에서 제초제를 뿌릴 시 박멸되는 나무의 그루 수를 계산하고,
                // 이 값이 최대가 되는 지점을 찾아줍니다.
                if(tree[i][j] <= 0) continue;
                int cnt = tree[i][j];
                for(int dir = 0; dir < 4; dir++) {
                    int nx = i;
                    int ny = j;
                    for(int x = 1; x <= k; x++) {
                        nx = nx + dx[dir];
                        ny = ny + dy[dir];
                        if(isOutRange(nx, ny)) break;
                        if(tree[nx][ny] <= 0) break;
                        cnt += tree[nx][ny];
                    }
                }
                if(maxDel < cnt) {
                    maxDel = cnt;
                    maxX = i;
                    maxY = j;
                }
            }
    
        ans += maxDel;
    
        // 찾은 칸에 제초제를 뿌립니다.
        if(tree[maxX][maxY] > 0) {
            tree[maxX][maxY] = 0;
            herb[maxX][maxY] = c;
            for(int dir = 0; dir < 4; dir++) {
                int nx = maxX;
                int ny = maxY;
                for(int x = 1; x <= k; x++) {
                    nx = nx + dx[dir];
                    ny = ny + dy[dir];
                    if(isOutRange(nx, ny)) break;
                    if(tree[nx][ny] < 0) break;
                    if(tree[nx][ny] == 0) {
                        herb[nx][ny] = c;
                        break;
                    }
                    tree[nx][ny] = 0;
                    herb[nx][ny] = c;
                }
            }
        }
    }
    
    // 제초제의 기간을 1년 감소시킵니다.
    public static void deleteHerb() {
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++) 
                if(herb[i][j] > 0) 
                    herb[i][j] -= 1;
    }

    public static void main(String[] args) {
        // 입력을 받는 등 초기 작업을 합니다.
        init();

        for(int i = 1; i <= m; i++) {
            // 1단계 : 인접한 네 개의 칸 중 나무가 있는 칸의 수만큼 나무가 성장합니다.
            stepOne();

            // 2단계 : 기존에 있었던 나무들은 아무것도 없는 칸에 번식을 진행합니다.
            stepTwo();

            // 제초제의 기간을 1년 감소시킵니다.
            deleteHerb();

            // 3단계 : 가장 많이 박멸되는 칸에 제초제를 뿌립니다.
            stepThree();
        }

        System.out.print(ans);
    }
}