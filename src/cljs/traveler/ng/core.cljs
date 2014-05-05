(ns traveler.ng.core
  (:use-macros [purnam.core :only [!]]
               [gyr.core :only [def.module def.controller def.directive]]))

(def.module traveler [])

(def.directive traveler.ngEnter []
  (fn [scope element attrs]
    (element.bind "keydown keypress" (fn [event]
                                       (if (== event.which 13)
                                         (scope.$apply (fn []
                                                         (scope.$eval attrs.ngEnter))))))))

(def.controller traveler.users-ctrl [$scope $http $window $filter]
  ;;modify these values to change the defaults
  (! $scope.gap 5)
  (! $scope.usersPerPage 10)

  ;;don't modify these values
  (! $scope.showPaging false)
  (! $scope.isSearch false)
  (! $scope.filteredUsers (array))
  (! $scope.pagedUsers (array))
  (! $scope.currentPage 1)
  (! $scope.query "")

  (! $scope.usersLoaded "loading")
  (! $scope.userPages 0)
  (! $scope.usersCount 0)
  (! $scope.users "")

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
                     (if (= $scope.query "")
                       ($scope.loadUsers)
                       (do
                         (! $scope.usersLoaded "loading")
                         (! $scope.isSearch true)
                         (! $scope.showPaging false)
                         (-> $http
                             (.get (str "api/user/search/" $scope.query))
                             (.success (fn [data]
                                         (if (empty? (aget data "results"))
                                           (! $scope.usersLoaded "no")
                                           (do
                                             (! $scope.users (aget data "results"))
                                             (! $scope.usersCount (count $scope.users))
                                             (! $scope.userPages (Math/ceil (/ $scope.usersCount $scope.usersPerPage)))
                                             (! $scope.usersLoaded "yes")))))
                             (.error (fn []
                                       (! $scope.users "")
                                       (! $scope.usersCount 0)
                                       (! $scope.userPages 0)
                                       (! $scope.usersLoaded "error"))))))))

  (! $scope.clearSearch (fn []
                          (! $scope.query "")
                          ($scope.search)))

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
                          (if (== (count $scope.users) 0)
                            0
                            (inc (* (dec $scope.currentPage) $scope.usersPerPage)))))

  (! $scope.showingTo (fn []
                        (if (== (count $scope.users) 0)
                          0
                          (if (>= (* $scope.currentPage $scope.usersPerPage) $scope.usersCount)
                            (+ (count $scope.users) (* (dec $scope.currentPage) $scope.usersPerPage))
                            (* $scope.usersPerPage $scope.currentPage)))))

  (! $scope.loadUsers (fn []
                        (! $scope.isSearch false)
                        (! $scope.usersLoaded "loading")
                        (-> $http
                            (.get (str "api/users/" $scope.usersPerPage "/" $scope.currentPage))
                            (.success (fn [data]
                                        (if (empty? (aget data "users"))
                                          (! $scope.usersLoaded "no")
                                          (do
                                            (! $scope.users (aget data "users"))
                                            (! $scope.usersCount (aget data "count"))
                                            (! $scope.userPages (Math/ceil (/ $scope.usersCount $scope.usersPerPage)))
                                            (! $scope.showPaging true)
                                            (! $scope.usersLoaded "yes")))))
                            (.error (fn []
                                      (! $scope.users "")
                                      (! $scope.usersCount 0)
                                      (! $scope.userPages 0)
                                      (! $scope.showPaging false)
                                      (! $scope.usersLoaded "error"))))))

  (! $scope.gotoUser (fn [user]
                       (! $window.location.href (str "user/" (aget user "id_sk")))))

  ;;Load the users to populate the table
  ($scope.loadUsers))

(def.controller traveler.user-ctrl [$scope $http]
  (! $scope.userFound "loading")
  (! $scope.user "")

  (! $scope.setUserId (fn [userId]
                        (! $scope.userId userId)
                        ($scope.getUser)))

  (! $scope.getUser (fn []
                      (-> $http
                          (.get (str "api/user/" $scope.userId))
                          (.success (fn [data]
                                      (! $scope.user (aget data "user"))
                                      (if (aget data "user" "username")
                                        (! $scope.userFound "yes")
                                        (! $scope.userFound "no"))))
                          (.error (fn [] (! $scope.userFound "error")))))))
