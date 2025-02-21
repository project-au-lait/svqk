package dev.aulait.svqk.arch.search;

import java.util.stream.IntStream;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class PageResultDto {
  @Schema(required = true)
  private long count;

  @Schema(required = true)
  private int start;

  @Schema(required = true)
  private long end;

  @Schema(required = true)
  private int lastPageNum;

  @Schema(required = true)
  private int[] pageNums;

  public PageResultDto build(long count, PageControlDto pageControl) {

    this.count = count;
    this.start = start(count, pageControl.getOffset());
    this.end = end(count, start, pageControl.getPageSize());

    this.lastPageNum = lastPageNum(count, pageControl.getPageSize());
    this.pageNums =
        pageNums(
            count,
            pageControl.getPageSize(),
            pageControl.getPageNumber(),
            pageControl.getPageNumsRange());

    return this;
  }

  static int start(long count, int offset) {
    return count < 1 ? 0 : offset + 1;
  }

  static long end(long count, int start, int pageSize) {
    return Math.min(start + pageSize - 1, count);
  }

  static int lastPageNum(long count, int pageSize) {
    return (int) Math.max(Math.ceil((double) count / pageSize), 0);
  }

  static int[] pageNums(long count, int pageSize, int pageNumber, int pageNumsRange) {
    return (count <= pageSize)
        ? new int[] {}
        : IntStream.rangeClosed(
                Math.max(pageNumber - pageNumsRange, 1),
                Math.min(pageNumber + pageNumsRange, lastPageNum(count, pageSize)))
            .toArray();
  }
}
