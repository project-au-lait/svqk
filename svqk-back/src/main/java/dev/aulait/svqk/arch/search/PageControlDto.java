package dev.aulait.svqk.arch.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.stream.LongStream;
import lombok.Data;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageControlDto {

  @Schema(required = true)
  private long count;

  @Schema(required = true)
  private int pageSize;

  @JsonIgnore private int pageNumber;

  @JsonIgnore private int pageNumsRange;

  @JsonIgnore private int offset;

  @Schema(required = true, implementation = Integer.class)
  @Getter(lazy = true)
  private final int start = count < 1 ? 0 : offset + 1;

  @Schema(required = true, implementation = Integer.class)
  @Getter(lazy = true)
  private final long end = Math.min(getStart() + pageSize - 1, count);

  @Schema(required = true, implementation = Integer.class)
  @Getter(lazy = true)
  private final int lastPage = (int) Math.max(Math.ceil((double) count / pageSize), 0);

  @Schema(required = true, implementation = Long.class, type = SchemaType.ARRAY)
  @Getter(lazy = true)
  private final long[] pageNums =
      (count <= pageSize)
          ? new long[] {}
          : LongStream.rangeClosed(
                  Math.max(pageNumber - pageNumsRange, 1),
                  Math.min(pageNumber + pageNumsRange, getLastPage()))
              .toArray();
}
