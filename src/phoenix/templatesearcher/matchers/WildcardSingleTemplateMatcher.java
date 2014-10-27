package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.support.ICharComparator;
import phoenix.templatesearcher.support.MultiReadableCharStream;
import phoenix.templatesearcher.support.Occurrence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WildcardSingleTemplateMatcher implements IMetaTemplateMatcher {
    /*
    ------- FULL ALGORITHM DESCRIPTION -------
    Each template is divided into the following parts: wildcard groups and usual characters groups.
    Each character group is searched in the given stream for O(streamSize + groupSize) time and O(groupSize) memory.
    Now we should merge group occurrences with consideration to wildcard groups between and around them.
    Suppose each group produces O(n = streamSize) occurrences (the worst case) and we have k groups.
    If we merge two groups with sizeA and sizeB occurrences, size of their merge will be min(sizeA, sizeB) and time to complete this merge is O(sizeA + sizeB) (two-pointers method).
    Now we can evaluate time of merging k groups into one group: O(2 * n * k/2) + O(2 * n * k/4) + ... + O(2 * n) =  O(n * k) = O(k * streamSize).
    In total we spend O(k * streamSize + templateSize) time and O(streamSize + templateSize) memory.
     */

    private static final int TEMPLATE_ID = 0;

    /*
    Full template scheme: wc[0] tp[0] wc[1] tp[1] ... wc[k - 2] tp[k - 2] wc[k - 1].
    k - count of '?' groups (the beginning and the ending group can have their size = 0)
    wc[i] = 'i' times '?' symbol;
    tp[i] = template part #i;
     */

    /**
     * wildcardParts[i] = count of wildcard symbols in i-th group
     */
    private ArrayList<Integer> wildcardParts;

    /**
     * templateParts[i] = i-th group of characters without wildcard symbols
     */
    private ArrayList<StringBuilder> templateParts;

    private SingleTemplateMatcher[] partMatchers;

    private int fullTemplateLength;

    public WildcardSingleTemplateMatcher() {
    }

    @Override
    public int addTemplate(String template)
            throws UnsupportedOperationException, IllegalArgumentException {
        if (templateParts != null) {
            throw new UnsupportedOperationException("Cannot add more then one template");
        }
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Template must not be empty");
        }

        fullTemplateLength = template.length();
        templateParts = new ArrayList<>();
        wildcardParts = new ArrayList<>();

        for (int wildcardID = 0, partID = 0, i = 0; i < fullTemplateLength; i++) {
            char c = template.charAt(i);
            if (c == ICharComparator.WILDCARD_SYMBOL) {
                if (i != 0 && template.charAt(i - 1) != ICharComparator.WILDCARD_SYMBOL) {
                    wildcardID++;
                }
                while (wildcardParts.size() <= wildcardID) {
                    wildcardParts.add(0);
                }
                wildcardParts.set(wildcardID, wildcardParts.get(wildcardID) + 1);
            } else {
                if (i != 0 && template.charAt(i - 1) == ICharComparator.WILDCARD_SYMBOL) {
                    if (!templateParts.isEmpty()) {
                        partID++;
                    }
                }
                while (templateParts.size() <= partID) {
                    templateParts.add(new StringBuilder());
                }
                templateParts.get(partID).append(c);
            }
        }

        // When no wildcard at the and, but wildcards present.
        if (wildcardParts.size() == templateParts.size()) {
            wildcardParts.add(0);
        } else if (wildcardParts.isEmpty()) {
            wildcardParts.add(0);
            wildcardParts.add(0);
        }

        partMatchers = new SingleTemplateMatcher[templateParts.size()];

        for (int i = 0, len = templateParts.size(); i < len; i++) {
            partMatchers[i] = new SingleTemplateMatcher();
            partMatchers[i].addTemplate(templateParts.get(i).toString());
        }

        return TEMPLATE_ID;
    }

    private List<IOccurrence> mergePartOccurrences(List<IOccurrence> leftMatches,
                                                   List<IOccurrence> rightMatches,
                                                   int wildcardsCount,
                                                   int rightLength) {
        Iterator<IOccurrence> leftIterator = leftMatches.iterator();
        Iterator<IOccurrence> rightIterator = rightMatches.iterator();

        //when some of occurrences becomes null, it means we should take next from iterator.
        IOccurrence leftOccurrence = null;
        IOccurrence rightOccurrence = null;

        int distanceBetweenEnds = rightLength + wildcardsCount;

        List<IOccurrence> mergeResult = new LinkedList<>();

        while (leftIterator.hasNext() || rightIterator.hasNext()) {
            if (leftOccurrence == null) {
                if (leftIterator.hasNext()) {
                    leftOccurrence = leftIterator.next();
                } else {
                    break;
                }
            }
            if (rightOccurrence == null) {
                if (rightIterator.hasNext()) {
                    rightOccurrence = rightIterator.next();
                } else {
                    break;
                }
            }

            int compare = rightOccurrence.getIndex() - leftOccurrence.getIndex();

            if (compare < distanceBetweenEnds) {
                rightOccurrence = null;
            } else if (compare == distanceBetweenEnds) {
                mergeResult.add(rightOccurrence);
                leftOccurrence = null;
                rightOccurrence = null;
            } else {
                leftOccurrence = null;
            }
        }

        return mergeResult;
    }

    @Override
    public List<IOccurrence> matchStream(ICharStream stream) {
        MultiReadableCharStream streamWrap = new MultiReadableCharStream(stream);

        ArrayList<Integer> dynamicWildcardParts = (ArrayList<Integer>) wildcardParts.clone();
        ArrayList<List<IOccurrence>> dynamicPartOccurrences = new ArrayList<>(templateParts.size());
        ArrayList<Integer> dynamicPartLengths = new ArrayList<>(templateParts.size());

        // Counting partial matches.
        for (int i = 0, partsCount = templateParts.size(); i < partsCount; i++) {
            List<IOccurrence> partOccurrences = partMatchers[i].matchStream(streamWrap);
            streamWrap.resetPosition();
            dynamicPartOccurrences.add(partOccurrences);
            dynamicPartLengths.add(templateParts.get(i).length());
        }

        // If the full template begins with wildcards.
        if (dynamicWildcardParts.get(0) > 0) {
            // We insert here phantom occurrences - possible beginning indices of template match - 1.
            List<IOccurrence> phantomOccurrences = new LinkedList<>();
            for (int i = 0, lastOccurrenceBegin = streamWrap.streamSize() - fullTemplateLength + 1;
                 i <= lastOccurrenceBegin; i++) {
                phantomOccurrences.add(new Occurrence(i - 1, TEMPLATE_ID));
            }
            dynamicPartOccurrences.add(0, phantomOccurrences);
            dynamicPartLengths.add(0, 1);
        } else {
            dynamicWildcardParts.remove(0);
        }

        // Now we suppose such full template scheme: tp[0] wc[0] ... tp[k - 1] {wc[k - 1]}.
        // wildcardParts.size() <= dynamicPartOccurrences.size() (max diff. = 1)

        if (dynamicWildcardParts.size() == dynamicPartOccurrences.size()) {
            if (dynamicWildcardParts.get(dynamicWildcardParts.size() - 1) > 0) {
                List<IOccurrence> phantomOccurrences = new LinkedList<>();
                // We insert here phantom occurrences - possible ending indices of template match (but with zero length).
                for (int i = fullTemplateLength - 1, lastOccurrenceEnd =
                        streamWrap.streamSize() - 1; i <= lastOccurrenceEnd; i++) {
                    phantomOccurrences.add(new Occurrence(i, TEMPLATE_ID));
                }
                dynamicPartOccurrences.add(phantomOccurrences);
                dynamicPartLengths.add(0);
            } else {
                dynamicWildcardParts.remove(dynamicWildcardParts.size() - 1);
            }
        }

        // Now we suppose the most convenient full template scheme: tp[0] wc[0] ... wc[k - 2] tp[k - 1].

        while (dynamicPartOccurrences.size() > 1) {
            ArrayList<Integer> newDynamicWildCardParts =
                    new ArrayList<>(dynamicWildcardParts.size() / 2);
            ArrayList<List<IOccurrence>> newDynamicPartOccurrences =
                    new ArrayList<>((dynamicPartOccurrences.size() + 1) / 2);
            ArrayList<Integer> newDynamicPartLengths =
                    new ArrayList<>((dynamicPartLengths.size() + 1) / 2);

            for (int i = 0, sz = dynamicPartOccurrences.size(); i < sz; i += 2) {
                if (i + 1 < sz) {
                    // We can merge two parts.
                    List<IOccurrence> left = dynamicPartOccurrences.get(i);
                    List<IOccurrence> right = dynamicPartOccurrences.get(i + 1);

                    int leftLength = dynamicPartLengths.get(i);
                    int rightLength = dynamicPartLengths.get(i + 1);

                    int wildcardsBetweenCount = dynamicWildcardParts.get(i);

                    List<IOccurrence> merged = mergePartOccurrences(
                            left, right, wildcardsBetweenCount, rightLength);

                    newDynamicPartOccurrences.add(merged);

                    // i-th wildcard group disappears.
                    if (i + 1 < dynamicWildcardParts.size()) {
                        newDynamicWildCardParts.add(dynamicWildcardParts.get(i + 1));
                    }
                    newDynamicPartLengths.add(leftLength + rightLength + wildcardsBetweenCount);
                } else {
                    // One last part that cannot be merged with anything.
                    newDynamicPartOccurrences.add(dynamicPartOccurrences.get(i));
                    newDynamicPartLengths.add(dynamicPartLengths.get(i));
                    // No wildcard groups are supposed to be after the last part.
                }
            }

            dynamicPartOccurrences = newDynamicPartOccurrences;
            dynamicPartLengths = newDynamicPartLengths;
            dynamicWildcardParts = newDynamicWildCardParts;
        }

        return dynamicPartOccurrences.get(0);
    }
}
