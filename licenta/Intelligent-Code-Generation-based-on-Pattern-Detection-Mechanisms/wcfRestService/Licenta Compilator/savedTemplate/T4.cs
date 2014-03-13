using System;   
using System.Collections.Generic;   
using System.Data;   
using System.Data.SqlClient;   
using System.Linq;   
using Catalyst.Data.Entities;   
using Catalyst.Entities.Customers;   
using Catalyst.SharedComponents;   
   
namespace Catalyst.Data.Repositories.Comments   
   
    public class CommentQueueRepository : StatelessSiteRepositoryBase, ICommentQueueRepository   
   
   
   
        public IList<commentqueue> Select(int siteId, DateTime actionTime, int topCount, int maxRetryCount)   </commentqueue>
   
            return SitesDatabase(siteId).Execute   
   
                "spr_CMS_Rating_Usage_QueueSelect",   
                CreateFromRecord,   
                SiteIdSqlParameter(siteId),   
                new SqlParameter("@ActionTime", actionTime),   
                new SqlParameter("@TopCount", topCount),   
   
            );   
        }   
   
   
    }   
}   
   
   
