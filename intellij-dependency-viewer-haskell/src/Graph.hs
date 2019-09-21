module Graph
    ( makeGraph
    , toDependencies
    ) where

import Types

import Data.Set (Set)
import qualified Data.Set as Set
import Debug.Trace (trace)

makeGraph :: Class -> Set Dependency -> Graph
makeGraph c allDeps =
  let neighbors = findRoute c Set.empty allDeps
  in Graph c neighbors

findRoute :: Class -> Set Dependency -> Set Dependency -> Set Node
findRoute c used allDeps =
  let neighbors = neigh c allDeps
      edges = Set.map (Dependency c) neighbors
      newNeigh  = Set.map dependencyTo $ edges Set.\\ used
      newUsed     = used `Set.union` edges
  in Set.map (\c -> toNode c newUsed allDeps) newNeigh
  where
    neigh c = Set.map dependencyTo . Set.filter ((==) c . dependencyFrom)

toNode :: Class -> Set Dependency -> Set Dependency -> Node
toNode c used all = Node c (findRoute c used all)

toDependencies :: Graph -> Set Dependency
toDependencies (Graph c neighbors) = toDependencies' c neighbors Set.empty

toDependencies' :: Class -> Set Node -> Set Dependency -> Set Dependency
toDependencies' c ns acc =
  let deps = Set.map (\(Node c' _) -> Dependency c c') ns
      ddeps = Set.toList $ Set.map (\(Node c ns') -> toDependencies' c ns' Set.empty) ns
  in Set.union deps (Set.unions ddeps)
