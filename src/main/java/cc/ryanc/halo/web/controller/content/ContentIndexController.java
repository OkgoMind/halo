package cc.ryanc.halo.web.controller.content;

import cc.ryanc.halo.model.enums.PostStatus;
import cc.ryanc.halo.model.enums.PostType;
import cc.ryanc.halo.model.vo.PostListVO;
import cc.ryanc.halo.service.OptionService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.web.controller.content.base.BaseContentController;
import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Blog index page controller
 *
 * @author : RYAN0UP
 * @date : 2019-03-17
 */
@Slf4j
@Controller
@RequestMapping
public class ContentIndexController extends BaseContentController {

    private final PostService postService;

    private final OptionService optionService;

    public ContentIndexController(PostService postService,
                                  OptionService optionService) {
        this.postService = postService;
        this.optionService = optionService;
    }


    /**
     * Render blog index
     *
     * @param model model
     * @return template path: /{theme}/post.ftl
     */
    @GetMapping
    public String index(Model model) {
        return this.index(model, 1, Sort.by(DESC, "topPriority").and(Sort.by(DESC, "createTime")));
    }

    /**
     * Render blog index
     *
     * @param model model
     * @param page  current page number
     * @return template path: /{theme}/post.ftl
     */
    @GetMapping(value = "page/{page}")
    public String index(Model model,
                        @PathVariable(value = "page") Integer page,
                        @SortDefault.SortDefaults({
                                @SortDefault(sort = "topPriority", direction = DESC),
                                @SortDefault(sort = "createTime", direction = DESC)
                        }) Sort sort) {
        log.debug("Requested index page, sort info: [{}]", sort);
        int pageSize = optionService.getPostPageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<PostListVO> posts = postService.pageListVoBy(PostStatus.PUBLISHED, PostType.POST, pageable);
        int[] rainbow = PageUtil.rainbow(page, posts.getTotalPages(), 3);
        model.addAttribute("is_index", true);
        model.addAttribute("posts", posts);
        model.addAttribute("rainbow", rainbow);
        return this.render("index");
    }
}