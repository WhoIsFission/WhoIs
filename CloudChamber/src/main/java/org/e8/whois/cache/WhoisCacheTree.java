package org.e8.whois.cache;
/**
 * 
 * This class is used for constructing Interval Tree which can hold range of IP Addresses
 * and provides mechanism of searching specific, generic and all ranges of IP address.
 * 
 */
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoisCacheTree<T>{

	private final static Logger logger=LoggerFactory.getLogger(WhoisCacheTree.class);
	private static WhoisCacheTree cacheInstance;
    private WhoIsNode<T> root;
    /*
     * private constructor for singleton instance
     * 
     */
    private WhoisCacheTree(){
    	
    }
    
    /**
     * 
     * Return single instance of WhoisCacheTree across application
     * 
     */
    
    public static WhoisCacheTree getCacheInstance(){
    	if(cacheInstance==null){
    		synchronized (WhoisCacheTree.class) {
				if(cacheInstance==null){
					cacheInstance=new WhoisCacheTree();
				}
			}
    	}
    	
   	return cacheInstance;
    }

/**
 * 
 * insert is used to insert range and helps to build tree 
 * which could be used for searching specific IP address.
 * 
 * @param l
 * @param h
 */
	public void insert(WhoIsNode<T> node){
		if(logger.isDebugEnabled())
			logger.debug("Insertion of node with start address: "+node.getStartAddress()+" has started");
		
		if(node==null)
			return;
        root=insert(root,node, node.low, node.high);
        
        logger.debug("Insertion of node with start address: "+node.getStartAddress()+" is completed");
    }
	
	/*
	 * To insert a specific range specified by low and high as nodes in a tree mentioned by node. 
	 *  
	 */
    private WhoIsNode<T> insert(WhoIsNode<T> node,WhoIsNode<T> nodeToInsert, T l, T h){
        if(node==null){
            return nodeToInsert;
        }
        else{
            int k=((Comparable)node.low).compareTo(l);
            if(k>0){
                node.left=insert(node.left,nodeToInsert, l, h);
            }
            else{
                node.right=insert(node.right,nodeToInsert, l, h);
            }
            node.height=Math.max(height(node.left), height(node.right))+1;
            node.max=findMax(node);
            int hd = heightDiff(node);
            if(hd<-1){
                int kk=heightDiff(node.right);
                if(kk>0){
                    node.right=rightRotate(node.right);
                    return leftRotate(node);
                }
                else{
                    return leftRotate(node);
                }
            }
            else if(hd>1){
                if(heightDiff(node.left)<0){
                    node.left = leftRotate(node.left);
                    return rightRotate(node);
                }
                else{
                    return rightRotate(node);
                } 
            }
            else;
        }
        return node;
    }
    
    /*
     * 
     * Left rotate the tree when height of right subtree is greater than 1
     */
    private WhoIsNode<T> leftRotate(WhoIsNode<T> n){
        WhoIsNode<T> r =  n.right;
        n.right = r.left;
        r.left=n;
        n.height=Math.max(height(n.left), height(n.right))+1;
        r.height=Math.max(height(r.left), height(r.right))+1;
        n.max=findMax(n);
        r.max=findMax(r);
        return r;
    }
    
    /*
     * Left rotate the tree when height of right subtree is greater than 1
     */
    
    private WhoIsNode<T> rightRotate(WhoIsNode<T> n){
        WhoIsNode<T> r =  n.left;
        n.left = r.right;
        r.right=n;
        n.height=Math.max(height(n.left), height(n.right))+1;
        r.height=Math.max(height(r.left), height(r.right))+1;
        n.max=findMax(n);
        r.max=findMax(r);
        return r;
    }
    
    /*
     * Compute the Height difference of left subtree- rightSubTree
     * 
     */
    private int heightDiff(WhoIsNode<T> a){
        if(a==null){
            return 0;
        }
        return height(a.left)-height(a.right);
    }
    
    /*
     * Computes the height of the tree.
     *
     */
    private int height(WhoIsNode<T> a){
        if(a==null){
            return 0;
        }
        return a.height;
    }
    
    
    /*
     * To compute the max value(including subtrees) or extreme right value in a range.
     * 
     */
    private T findMax(WhoIsNode<T> n){
    	n.max=n.high;// done for deletion
        if(n.left==null && n.right==null){
            return n.max;
        }
        if(n.left==null){
            if(((Comparable)n.right.max).compareTo(n.max)>0){
                return n.right.max;
            }
            else{
                return n.max;
            }
        }
        if(n.right==null){
           if(((Comparable)n.left.max).compareTo(n.max)>0){
                return n.left.max;
            }
            else{
                return n.max;
            } 
        }
        Comparable c1 = (Comparable)n.left.max;
        Comparable c2 = (Comparable)n.right.max;
        Comparable c3 = (Comparable)n.max;
        T max=null;
        if(c1.compareTo(c2)<0){
            max=n.right.max;
        }
        else{
            max=n.left.max;
        }
        if(c3.compareTo((Comparable)max)>0){
            max=n.max;
        }
        return max;
    }

/**
 * 
 * Search specific Interval where given Key lies
 * 
 * @param keySearch
 * @return
 */
public WhoIsNode searchSpecificInterval(T keySearch){
	if(logger.isDebugEnabled())
		logger.debug("Started searching specific address range for key : "+keySearch);
	
	if(root==null)
		return null;
	
	
	return searchSpecificIntervalHelper(root, keySearch);
	
}


/*
 * Helper method for searching specifiv interval 
 * 
 */
private WhoIsNode searchSpecificIntervalHelper(WhoIsNode<T> nod,T keySearch){
	if(nod==null)
		return null;
	
	WhoIsNode<T> left=null;
	WhoIsNode<T> right=null;
	WhoIsNode<T> curr=null;
	WhoIsNode<T> returnNode=null;
	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
		left=searchSpecificIntervalHelper(nod.left,keySearch);
    }
   
	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
		right=searchSpecificIntervalHelper(nod.right,keySearch);
     }
	if(isInside(nod, keySearch)){
		curr=nod;
	}	
	if(left==null&&right==null)
		return curr;
	
	if(left!=null&&(right==null||left.compareTo(right)<=0))
		returnNode=left;
	else
		returnNode=right;
	
	if(returnNode!=null&&(curr==null||returnNode.compareTo(curr)<=0))
		return returnNode;
	else
		return curr;

}

/**
 * 
 * Search Generic interval where given key lies
 * 
 * @param keySearch
 * @return
 */
public WhoIsNode searchGenericInterval(T keySearch){
	

	if(logger.isDebugEnabled())
		logger.debug("Started searching for generic IP range for key : "+keySearch);
	if(root==null)
		return null;
	return searchGenericIntervalHelper(root, keySearch);
}

/*
 * 
 * Helper method for searching Generic interval
 */

private WhoIsNode searchGenericIntervalHelper(WhoIsNode<T> nod,T keySearch){
	
	if(nod==null)
		return null;
	
	WhoIsNode<T> left=null;
	WhoIsNode<T> right=null;
	WhoIsNode<T> curr=null;
	WhoIsNode<T> returnNode=null;
	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
		left=searchSpecificIntervalHelper(nod.left,keySearch);
    }
   
	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
		right=searchSpecificIntervalHelper(nod.right,keySearch);
     }
	if(isInside(nod, keySearch)){
		curr=nod;
	}
	
	if(left==null&&right==null)
		return curr;
	if(left!=null&&(right==null||left.compareTo(right)>=0))
		returnNode=left;
	else
		returnNode=right;
	
	if(returnNode!=null&&(curr==null||returnNode.compareTo(curr)>=0))
		return returnNode;
	else
		return curr;

}

/**
 * Search all the intervals where given key lies
 * 
 * @param keySearch
 * @return
 */

public Set<WhoIsNode> setOfIntervalSearch(T keySearch){
	if(logger.isDebugEnabled())
		logger.debug("Started searching all IP ranges for key : "+keySearch);
	if(root==null)
		return null;
	return intervalSearchForListHelper(root, keySearch);
}


/*
 * Search all the intervals where given lies.
 * 
 */

private Set<WhoIsNode> intervalSearchForListHelper(WhoIsNode<T> nod,T keySearch){
    if(nod==null)
    	return Collections.EMPTY_SET;
    Set<WhoIsNode> setOfNodes=new TreeSet<WhoIsNode>();
    	if(isInside(nod, keySearch)){
    		setOfNodes.add(nod);
    	}
    	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
    		setOfNodes.addAll(intervalSearchForListHelper(nod.left,keySearch));
        }
       
    	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
    		setOfNodes.addAll(intervalSearchForListHelper(nod.right,keySearch));
         }
    return setOfNodes;
}

/*
 * verifies whether key(t) is inside the range mentioned.
 * 
 */

private boolean isInside(WhoIsNode<T> node, T t){
        Comparable cLow=(Comparable)node.low;
        Comparable cHigh=(Comparable)node.high;
        int i = cLow.compareTo(t);
        int j = cHigh.compareTo(t);
        if(i<=0 && j>=0){
            return true;
        }
        return false;
    }
/**
 * Returns the height of the tree
 * 
 * @return
 */
public int getHeight(){
	return root.height;
}

/**
 * 
 * Clearing entire cache
 */
public void deleteCache(){
	this.root=null;
}



/*
 * Helper method for deleting specific interval 
 * 
 */
private DeleteWhoisEntry<T> deleteSpecificInterval(WhoIsNode<T> nod,WhoIsNode<T> parent,T keySearch){
	if(nod==null)
		return null;
	
	DeleteWhoisEntry<T> left=null;
	DeleteWhoisEntry<T> right=null;
	DeleteWhoisEntry<T> curr=null;
	DeleteWhoisEntry<T> returnNode=null;
	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
		left=deleteSpecificInterval(nod.left,nod,keySearch);
    }
   
	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
		right=deleteSpecificInterval(nod.right,nod,keySearch);
     }
	if(isInside(nod, keySearch)){
		curr=new DeleteWhoisEntry<T>();
		curr.aNode=nod;
		curr.aParent=parent;
	}	
	if(left==null&&right==null)
		return curr;
	
	if(left!=null&&(right==null||(left).compareTo(right)<=0))
		returnNode=left;
	else
		returnNode=right;
	
	if(returnNode!=null&&(curr==null||returnNode.compareTo(curr)<=0))
		return returnNode;
	else
		return curr;

}

/**
 * Deleting specific IP range in a cache
 * @param keySearch
 */
public void deleteCacheEntry(T keySearch){
	if(logger.isDebugEnabled())
		logger.debug("Started deleting for cache entry for key : "+keySearch);
	
	DeleteWhoisEntry<T> delNode=deleteSpecificInterval(root,null, keySearch);
	if(delNode!=null)
	deleteCacheEntry(delNode.aNode, delNode.aParent);
	
	if(logger.isDebugEnabled())
		logger.debug("Deleting of cache entry completed for key : "+keySearch);
}

/*
 * Private delete cache helper method
 * 
 */

private void deleteCacheEntry(WhoIsNode<T> node,WhoIsNode<T> parent){
	
		//TODO delete it
		if(node.left==null&&node.right==null){
			if(parent!=null){
			if(parent.left==node){
				parent.left=null;
			}else 
				parent.right=null;
			}else{
				root=null;
			}
		}else if(node.left==null){
			if(parent!=null){
			if(parent.left==node){
				parent.left=node.right;
			}else 
				parent.right=node.right;
			}else{
				root=node.right;
			}
		}else if(node.right==null){
			if(parent!=null){
			if(parent.left==node){
				parent.left=node.left;
			}else 
				parent.right=node.left;
			}else{
				root=node.left;
			}
			}else{
				WhoIsNode<T> nodeToInsert=getSucessorNode(node,null);
				if(parent!=null){
					if(parent.left==node){
					parent.left=nodeToInsert;
				}else 
					parent.right=nodeToInsert;
				}
				nodeToInsert.left=node.left;
				nodeToInsert.right=node.right;
				if(parent==null){
					root=nodeToInsert;
				}
				
			}
		root=balanceTree(root);
		node=null;
		
}

/*
 * Getting the successor node
 * 
 */
private WhoIsNode<T> getSucessorNode(WhoIsNode<T> node,WhoIsNode<T> parent){
	parent=node;
	node=node.right;
	boolean flag=true;
	while(node.left!=null){
		parent=node;
		node=node.left;
		flag=false;
	}
	if(!flag)
		parent.left=node.right;
	else
		parent.right=node.right;
return node;
}

/*
 * 
 * Balancing the tree
 * 
 */
private WhoIsNode<T> balanceTree(WhoIsNode<T> node){
	if(node==null)
		return null;

	node.left=balanceTree(node.left);
	node.right=balanceTree(node.right);

	node.height=Math.max(height(node.left), height(node.right))+1;
    node.max=findMax(node);
	int hd = heightDiff(node);
    if(hd<-1){
        int kk=heightDiff(node.right);
        if(kk>0){
            node.right=rightRotate(node.right);
            return leftRotate(node);
        }
        else{
            return leftRotate(node);
        }
    }
    else if(hd>1){
        if(heightDiff(node.left)<0){
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        else{
            return rightRotate(node);
        } 
    }
    else return node;
}

/**
 * 
 * Helper class for DeleteWhoisEntry
 * @author Abhijit
 *
 * @param <T>
 */
private class DeleteWhoisEntry<T> implements Comparable<DeleteWhoisEntry<T>>{
	WhoIsNode<T> aNode;
	WhoIsNode<T> aParent;

	 DeleteWhoisEntry(){
		 
	 }

	@Override
	public int compareTo(DeleteWhoisEntry<T> o) {
		// TODO Auto-generated method stub
		return this.aNode.compareTo(o.aNode);
	}
}
}