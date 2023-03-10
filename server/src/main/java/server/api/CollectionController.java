/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Collection;
import server.database.CollectionRepository;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    private final CollectionRepository repo;

    /**
     * Controller constructor
     * @param repo repo reference
     */
    public CollectionController(CollectionRepository repo) {
        this.repo = repo;

        // TODO remove this when testing is done
        this.repo.save(new Collection("test1"));
        this.repo.save(new Collection("test2"));
    }

    /**
     * Hardcoded mapping all collections
     * @return List of collections objectss
     */
    @GetMapping(path = { "", "/" })
    public List<Collection> getAll() {
        return repo.findAll();
    }

    /**
     * getById method for a Collection with path var
     * @param id of Collection
     * @return a response Entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getById(@PathVariable("id") String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Initalization of a collection
     * @param collection a new collection obj
     * @return the Response Entity
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Collection> add(@RequestBody Collection collection) {

        Collection saved = repo.save(collection);
        return ResponseEntity.ok(saved);
    }

}