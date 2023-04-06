package client.services;

import client.utils.ServerUtils;
import commons.Tag;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class TagOverviewService {

    private final ServerUtils serverUtils;

    /**
     * This is the constructor that'll be used to inject this service into the tag overview controller
     * @param serverUtils the server that'll be later used to retrieve tags
     */
    @Inject
    public TagOverviewService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * This method uses the provided boardId to get the necessary tags
     * @param boardId the board that the tags belong to
     * @return the tags that are needed by the client
     */
    public List<Tag> getTags(Long boardId) {
        return serverUtils.getTags(boardId);
    }
}
