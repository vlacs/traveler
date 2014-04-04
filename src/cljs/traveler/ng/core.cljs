(ns traveler.ng.core
  (:require [traveler.ng.userdata :refer [userdata]])
  (:use-macros [purnam.core :only [!]]
               [gyr.core :only [def.module def.controller def.directive]]))

(def.module users-module [])

(def.directive users-module.ngEnter []
  (fn [scope element attrs]
    (element.bind "keydown keypress" (fn [event]
                                       (if (== event.which 13)
                                         (scope.$apply (fn []
                                                         (scope.$eval attrs.ngEnter))))))))

(def.controller users-module.users-ctrl [$scope $filter]
  ;;modify these values to change the defaults
  (! $scope.gap 5)
  (! $scope.usersPerPage 10)

  ;;don't modify these values
  (! $scope.filteredUsers (array))
  (! $scope.pagedUsers (array))
  (! $scope.currentPage 1)
  (! $scope.query "")

  ;;this controller uses a data file that cannot be committed
  ;;to source control, put yours here.
  (! $scope.users userdata)

  (defn searchMatch
    "Use this function to find a needle in the haystack!"
    [haystack needle]
    (if (or (not needle) (if (number? needle) false (empty? needle)))
      true
      (if (number? haystack)
        (if (number? needle)
          (== haystack needle)
          false)
        (if (number? needle)
          false
          (not= (.indexOf (.toLowerCase haystack) (.toLowerCase needle)) -1)))))

  (! $scope.search (fn []
                     (! $scope.filteredUsers (($filter "filter")
                                              $scope.users (fn [user]
                                                              (if (not $scope.query)
                                                                true)
                                                              (let [search-fields (array (aget user "id") (aget user "username") (aget user "firstname") (aget user "lastname") (aget user "privilege"))
                                                                    split-query (.split $scope.query #" ")
                                                                    match (atom 0)]
                                                                (doseq [q split-query]
                                                                  (doseq [field search-fields]
                                                                    (if (searchMatch field q)
                                                                      (reset! match (inc @match)))))
                                                                (if (>= @match (count split-query))
                                                                  true
                                                                  false)))))
                     (! $scope.currentPage 1)
                     ($scope.groupToPages)))

  (! $scope.groupToPages (fn []
                           (! $scope.pagedUsers (array))
                           (doseq [[i user] (map vector (iterate inc 0) $scope.filteredUsers)]
                             (if (nil? (nth $scope.pagedUsers (Math/floor (/ i $scope.usersPerPage))))
                               (do
                                 (.push $scope.pagedUsers (array))
                                 (.push (nth $scope.pagedUsers (Math/floor (/ i $scope.usersPerPage))) user))
                               (.push (nth $scope.pagedUsers (Math/floor (/ i $scope.usersPerPage))) user)))))

  (! $scope.range (fn [num-pages cur-page]
                    (let [ret (array)
                          calc-gap (+ 1 (- num-pages cur-page))]
                      (if (> num-pages $scope.gap)
                        (if (> $scope.gap calc-gap)
                          (dotimes [i $scope.gap]
                            (.push ret (+ i (inc (- num-pages $scope.gap)))))
                          (dotimes [i $scope.gap]
                            (.push ret (+ cur-page i))))
                        (dotimes [i calc-gap]
                          (.push ret (+ cur-page i))))
                      ret)))

  (! $scope.prevPage (fn []
                       (if (> $scope.currentPage 1)
                         (! $scope.currentPage (dec $scope.currentPage)))))

  (! $scope.nextPage (fn []
                       (if (< $scope.currentPage (count $scope.pagedUsers))
                         (! $scope.currentPage (inc $scope.currentPage)))))

  (! $scope.setPage (fn []
                      (! $scope.currentPage this.n)))

  (! $scope.showingFrom (fn []
                          (if (== (count $scope.filteredUsers) 0)
                            0
                            (inc (* (dec $scope.currentPage) $scope.usersPerPage)))))

  (! $scope.showingTo (fn []
                        (if (== (count $scope.filteredUsers) 0)
                          0)
                        (if (== $scope.currentPage (count $scope.pagedUsers))
                          (count $scope.filteredUsers)
                          (* $scope.currentPage $scope.usersPerPage))))

  ;;Run the search to populate the table
  ($scope.search))
