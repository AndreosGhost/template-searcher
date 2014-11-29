package phoenix.templatesearcher.test.matchers.once;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import phoenix.templatesearcher.api.IMetaTemplate2DMatcher;
import phoenix.templatesearcher.matchers.SingleTemplate2DMatcher;
import phoenix.templatesearcher.support.Matrix2D;
import phoenix.templatesearcher.test.matchers.IMatcher2DTest;

import static phoenix.templatesearcher.test.matchers.MatcherTester.*;

@RunWith(JUnit4.class)
public class SingleTemplate2DMatcherTestOnce implements IMatcher2DTest {
    @Override
    public IMetaTemplate2DMatcher obtainFreshMatcher() {
        return new SingleTemplate2DMatcher();
    }

    @Test
    public void testFindTemplateOneSymbolInStreamOneSymbol() {
        testMatchMatrix(
                makeTestData(
                        2,
                        2,
                        2,
                        2,
                        5,
                        5,
                        5,
                        5,
                        RowSupplier.singleCharRowSupplier('a'),
                        RowSupplier.singleCharRowSupplier('a')));
    }

    @Test
    public void testFindTemplateOneSymbolInStream() {
        RowSupplier aSupplier = RowSupplier.singleCharRowSupplier('a');
        RowSupplier bSupplier = RowSupplier.singleCharRowSupplier('b');

        int templateSize = 2;
        int searchableSize = 5;

        testMatchMatrix(
                makeTestData(
                        templateSize,
                        templateSize,
                        templateSize,
                        templateSize,
                        searchableSize,
                        searchableSize,
                        searchableSize,
                        searchableSize,
                        aSupplier,
                        (rowID, length) -> {
                            if (rowID <= templateSize) {
                                return aSupplier.supplyRow(rowID, templateSize) + bSupplier
                                        .supplyRow(rowID, length - templateSize);
                            } else {
                                return bSupplier.supplyRow(rowID, length);
                            }
                        }));
    }

    @Test
    public void testFindMatrixWithLastLineEqualToFirst() {
        testMatchMatrix(
                new Matrix2D("aba", "bab", "aba"), new Matrix2D("abacaba", "bababab", "ababcab", "cababba"));
    }

    @Test
    public void testFindMatrixWithNoEqualLines() {
        testMatchMatrix(
                new Matrix2D("abc", "bce", "ced"), new Matrix2D("abcde", "fdcab", "eabca", "abcef", "ccedk"));
    }

    @Test
    public void testFindMatrix() {
        testMatchMatrix(
                new Matrix2D("aba", "acb"),
                new Matrix2D("abaca", "aacba", "aabac", "cacba", "bcaba", "abacb", "acbba"));
    }

    @Test
    public void testFindMatrix1() {
        testMatchMatrix(
                new Matrix2D("ab", "aa"), new Matrix2D("baba", "aaab", "abaa", "aabb"));
    }

}