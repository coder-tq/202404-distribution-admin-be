package com.codertq.selleradmin.controller;

import com.codertq.selleradmin.domain.pojo.Result;
import com.codertq.selleradmin.domain.vo.CategoryVO;
import com.codertq.selleradmin.domain.vo.request.CreateCategoryRequest;
import com.codertq.selleradmin.domain.vo.request.UpdateCategoryListRequest;
import com.codertq.selleradmin.domain.vo.request.UpdateCategoryRequest;
import com.codertq.selleradmin.mpservice.CategoryMPService;
import com.codertq.selleradmin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/20
 */
@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "CategoryController", description = "类别接口")
public class CategoryController {
    @Autowired
    private CategoryMPService categoryMPService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "根据时间戳查询当天分类信息")
    public Result<List<CategoryVO>> getCurrentCategoryList(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date) {
        return Result.success(categoryMPService.getCurrentCategoryList(date));
    }


    @PostMapping("/updateListByDate")
    @Operation(summary = "根据时间戳更新某一天分类信息")
    public Result<Boolean> updateCategoryList(@RequestBody UpdateCategoryListRequest request) {
        return Result.success(categoryMPService.updateCategoryList(request));
    }

    @PostMapping("/updateByDate")
    @Operation(summary = "根据时间更新分类信息")
    public Result<Boolean> updateCategory(@RequestBody UpdateCategoryRequest request) {
        return Result.success(categoryMPService.updateCategory(request));
    }

    @PostMapping("/createCategory")
    @Operation(summary = "创建分类信息")
    public Result<Boolean> createCategory(@RequestBody CreateCategoryRequest request) {
        return Result.success(categoryService.createCategory(request));
    }

    @PostMapping("/deleteCategory")
    @Operation(summary = "删除分类信息")
    public Result<Boolean> deleteCategory(@RequestParam("id") String id) {
        return Result.success(categoryService.deleteCategory(id));
    }
}
