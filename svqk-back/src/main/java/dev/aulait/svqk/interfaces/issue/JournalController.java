package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.web.ApiPath;
import dev.aulait.svqk.domain.issue.JournalService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@Path(JournalController.JOUNAL_PATH)
@RequiredArgsConstructor
public class JournalController {

  private final JournalService service;

  static final String JOUNAL_PATH = ApiPath.ROOT + "/jounal";

  // TODO save
}
