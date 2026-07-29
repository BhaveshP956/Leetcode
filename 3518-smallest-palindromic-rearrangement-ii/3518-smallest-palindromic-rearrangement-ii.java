class Solution {
    static final long CAP = 1_000_001L;

    List<Integer> primes = new ArrayList<>();

    public String smallestPalindrome(String s, int k) {
        sieve(s.length() / 2);

        int[] half = new int[26];
        char mid = 0;

        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;

        for (int i = 0; i < 26; i++) {
            half[i] = freq[i] / 2;
            if ((freq[i] & 1) == 1) mid = (char) ('a' + i);
        }

        if (countWays(half) < k) return "";

        int halfLen = s.length() / 2;
        StringBuilder left = new StringBuilder();

        for (int pos = 0; pos < halfLen; pos++) {
            for (int c = 0; c < 26; c++) {
                if (half[c] == 0) continue;

                half[c]--;
                long ways = countWays(half);

                if (ways >= k) {
                    left.append((char) ('a' + c));
                    break;
                } else {
                    k -= ways;
                    half[c]++;
                }
            }
        }

        StringBuilder ans = new StringBuilder();
        ans.append(left);
        if (mid != 0) ans.append(mid);
        ans.append(new StringBuilder(left).reverse());

        return ans.toString();
    }

    private void sieve(int n) {
        boolean[] comp = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            if (!comp[i]) {
                primes.add(i);
                if ((long) i * i <= n) {
                    for (int j = i * i; j <= n; j += i)
                        comp[j] = true;
                }
            }
        }
    }

    // Number of distinct permutations of the multiset (capped).
    private long countWays(int[] cnt) {
        int total = 0;
        for (int x : cnt) total += x;

        long res = 1;

        for (int p : primes) {
            int exp = factExp(total, p);
            for (int x : cnt)
                if (x > 1)
                    exp -= factExp(x, p);

            if (exp > 0) {
                res = mulPowCap(res, p, exp);
                if (res >= CAP) return CAP;
            }
        }

        return res;
    }

    private int factExp(int n, int p) {
        int e = 0;
        while (n > 0) {
            n /= p;
            e += n;
        }
        return e;
    }

    private long mulPowCap(long cur, long base, int exp) {
        long b = base;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                cur *= b;
                if (cur >= CAP) return CAP;
            }
            exp >>= 1;
            if (exp > 0) {
                b *= b;
                if (b >= CAP) b = CAP;
            }
        }
        return Math.min(cur, CAP);
    }
}