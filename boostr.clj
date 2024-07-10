(config
(text-field
: name "baseUrl"
: label "Base URL"
: placeholder "Enter the base URL"
)

(text-field
:name "email"
:label "Username"
:placeholder "Enter your username"
)

(password-field
:name "password"
:label "Password"
:placeholder "Enter your password"
))



(default-source (http/get :base-url "https://app.boostr.com/api")
  (auth/session-token
    (auth-params
      (header-params "Authorization" "{jwt}"
                     "Accept" "application/vnd.boostr.public")))
    (source
      (http/post
        :base-url "https://app.boostr.com/api"
        :url "/user_token"
        
    (body-params
    {
    auth: {
        email: "USER_EMAIL",
        password: "USER_PASSWORD"
          }
    }
    )

    (extract-path ""))
    (fields
      jwt))

    (paging/page-number
   :page-number-query-param-initial-value 0
   :page-number-query-param-name "page"
   :limit 50 ;;optional
   :limit-query-param-name "per" ;;optional
  ))


(entity ACTIVITY
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/activities")
  (extract-path ""))
  
(fields
 id :id
 username
 activity_type_name
 happened_at
 created_by
 updated_by
 created_at
 updated_at)

   (dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "agency")
    (flatten-fields
      (fields
        id
        name)
      :from "advertiser")
    (flatten-fields
      (fields
        id
        name)
      :from "deal")
    (flatten-fields
      (fields
        id
        name)
      :from "lead")
    (flatten-fields
      (fields
        id
        name)
      :from "publisher"))

  (relate (contains-list-of ACTIVITY_CLIENT_AVERTISER :inside-prop "client/advertisers")
          (contains-list-of ACTIVITY_CLIENT_AGENCY    :inside-prop "client/agencies")
          (contains-list-of ACTIVITY_CONTACT          :inside-prop "contacts"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional
          

(entity ACTIVITY_CLIENT_AVERTISER
(api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
(fields
 id :id
 name)
(relate
(needs ACTIVITY : prop "id"))
)

(entity ACTIVITY_CLIENT_AGENCY
(api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
(fields
 id :id
 name)
(relate
(needs ACTIVITY :prop "id"))
)

(entity ACTIVITY_CONTACT
(api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
(fields
 id :id
 name)
(relate
(needs ACTIVITY : prop "id"))
)



(entity ACCOUNT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/accounts")
  (extract-path ""))
  

(fields
 id :id
 name
 website
 type(json)
 catagory
 sub_category
 parent_account
 parent_account_id
 region
 segment
 holding_company
 shell_record
 note
 legacy_id
 discount
 address(json)
 created_by
 updated_by
 created_at
 updated_at)

 (dynamic-fields(custom-fields :from "custom_fields" :prefix "custom_"))

(relate (contains-list-of ACCOUNT_CLIENT_CONTACT :inside-prop "client_contacts"))

(sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))



(entity ACCOUNT_CLIENT_CONTACT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (fields
  contact_id :id 
  is_active
  primary)
  
 (relate (needs ACCOUNT : prop "id")))



(entity CONTACT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/contacts")
  (extract-path ""))
  
(fields
 id :id
 name
 first_name
 last_name
 middle_name
 title
 position
 works_at
 contact_type
 created_by
 updated_by
 created_at
 updated_at
 address(json))

  (dynamic-fields(custom-fields :from "custom_fields" :prefix "custom_"))
  (relate (contains-list-of ACCOUNT_CONTACT :inside-prop "account_contacts"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional


(entity ACCOUNT_CONTACT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (fields
  id :id
  account_id 
  is_active)

 (dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "account"))
 
 (relate (needs CONTACT : prop "id")))


(entity CONTENT_FEE
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/content_fees")
  (extract-path ""))
  
(fields
 id :id
 io_id
 io_name
 product_name
 product_id
 updated_at
 start_date
 end_date
 budget_loc
 budget
 month_budget_delivered_loc(json)
 month_budget_delivered(json))

  (dynamic-fields(custom-fields :from "custom_fields" :prefix "custom_"))

  (relate (contains-list-of CONTENT_PRODUCT_BUDGET :inside-prop "product_budgets"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional


(entity CONTENT_PRODUCT_BUDGET
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  index :id :index
  month
  budget_loc
  budget)

 (relate (needs CONTENT_FEE : prop "id")))



(entity CONTRACT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/contracts")
  (extract-path ""))
  
(fields
 id :id
 name
 restricted
 start_date
 end_date
 signed_date
 status
 type
 auto_renew
 currency
 created_by
 created_at
 updated_at)

(dynamic-fields
 (custom-fields :from "custom_fields" :prefix "custom_")

    (flatten-fields
      (fields
        id
        start_date
        end_date
        name
        budget)
      :from "deal")
      
      (flatten-fields
      (fields
        id
        name)
      :from "deal.advertiser")
      
      (flatten-fields
      (fields
        id
        name)
      :from "deal.agency")

      (flatten-fields
      (fields
        id
        name)
      :from "advertiser")
      (flatten-fields
      (fields
        id
        name)
      :from "agency"))


  (relate (contains-list-of CONTRACT_CONTACT :inside-prop "contacts")
          (contains-list-of CONTRACT_TEAM :inside-prop "team"))
          
  (sync-plan
    (change-capture-cursor)
    
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional


(entity CONTRACT_CONTACT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 first_name
 last_name
 middle_name
 title
 email
 works_at
 address(json))

 (relate (needs CONTRACT : prop "id")))


(entity CONTRACT_TEAM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 role)

 (relate (needs CONTRACT : prop "id")))


(entity DEAL_PRODUCTS_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/deal_products")
  (extract-path ""))
  
(fields
 id :id
 deal_id
 budget
 budget_loc
 created_at
 updated_at
 start_date
 end_date
 term
 period
 show_in_forecast)

   (dynamic-fields
    (custom-fields :from "custom_fields" :prefix "custom_")

    (flatten-fields
      (fields
        id
        name
        full_name
        level
        )
      :from "product")
    (flatten-fields
      (fields
        id
        name
        level)
      :from "product.parent")
    (flatten-fields
      (fields
        id
        name
        level)
      :from "product.top_parent")
    (flatten-fields
      (fields
        id
        name)
      :from "territory"))

  (relate (contains-list-of DEAL_PRODUCT_BUDGET :inside-prop "client/advertisers"))
          
  (sync-plan
    (change-capture-cursor)
   
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity DEAL_PRODUCT_BUDGET
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 index :id :index
 month
 budget_loc
 budget)

 (relate (needs DEAL_PRODUCTS_ITEM : prop "id")))


(entity DEAL
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/deals")
  (extract-path ""))
  
(fields
 id :id
 start_date
 end_date
 name
 type
 source
 next_steps
 stage_name
 stage_id
 deal_type
 advertiser_id
 advertiser_name
 agency_id
 agency_name
 created_at
 updated_at
 currency
 budget
 budget_loc
 lead_id
 closed_at
 closed_comments
 closed_reason)

   (dynamic-fields(custom-fields :from "custom_fields" :prefix "custom_"))



  (relate (contains-list-of DEAL_MEMBER  :inside-prop "deal_members")
          (contains-list-of DEAL_CONTACT :inside-prop "deal_contacts")
          (contains-list-of DEAL_INTEGRATIONS :inside-prop "integrations"))
          
  (sync-plan
    (change-capture-cursor)
   
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity DEAL_MEMBER
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 user_id
 email
 share
 type
 role
 seller_type
 product_id)

 (relate (needs DEAL : prop "id")))


(entity DEAL_CONTACT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 first_name
 last_name
 middle_name
 title
 email
 phone
 mobile
 works_at
 position
 role)

 (relate (needs DEAL : prop "id")))



(entity DEAL_INTEGRATIONS
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 external_id
 external_type)

 (relate (needs DEAL : prop "id")))




(entity DISPLAY_LINE_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/display_line_items")
  (extract-path ""))
  
(fields
 id :id
 line_number
 ad_server
 price
 pricing_type
 start_date
 end_date
 line_quantity
 line_quantity_delivered
 line_budget_loc
 line_budget
 line_budget_delivered_loc
 line_budget_delivered
 is_pg
 created_at
 updated_at)

 (dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "io")
      
    (flatten-fields
      (fields
        id
        name)
      :from "product"))
 


  (relate (contains-list-of DISPLAY_LINE_MONTHLY_BUDGET :inside-prop "monthly_budgets"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity DISPLAY_LINE_MONTHLY_BUDGET
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 index :id :index
 month
 budget_delivered
 budget_delivered_loc
 quantity_delivered)
 
 (relate(needs DISPLAY_LINE_ITEM :  prop "id")))



(entity INTEGRATION
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/integrations")
  (extract-path ""))
  
(fields
 id :id
 integratable_id
 integratable_type
 external_id
 external_type
 created_at
 updated_at
 external_text_id
 company_id))



(entity IO_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/ios")
  (extract-path ""))
  
(fields
 id :id
 rate_card_id
 external_io_number
 pg_id
 io_number
 io_quantity_booked
 io_ecpm
 deal_id
 status
 calendar_type
 budget_loc
 budget
 budget_booked_loc
 budget_booked
 currency
 start_date
 end_date
 created_at
 updated_at
 discount
 gross_budget
 gross_budget_loc)


(dynamic-fields
  (custom-fields :from "custom_fields" :prefix "custom_")

    (flatten-fields
      (fields
        id
        name)
      :from "advertiser")
      
      (flatten-fields
      (fields
        id
        name)
      :from "agency"))


  (relate (contains-list-of IO_MEMBER  :inside-prop "io_members")
          (contains-list-of DEAL_CONTACT :inside-prop "discounts"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity IO_MEMBER
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 email
 share
 from_date
 to_date
 product_id
 seller_type
 name
 role)

 (relate (needs IO_ITEM : prop "id")))




(entity IO_LINE_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/ios")
  (extract-path ""))
  
(fields
 id :id
 available_units
 line_name
 io_id
 package_id
 package_name
 io_name
 io_quantity_booked
 io_ecpm
 rate_card
 product_name
 product_id
 rate_type
 rate_type_id
 line_start_date
 line_end_date
 line_actual_cost_loc
 line_estimated_cost_loc
 cost_adjustment
 rate_booked_loc
 rate_booked
 quantity_type
 is_added_value
 line_quantity_booked
 line_quantity_delivered
 line_budget_booked_loc
 line_budget_booked
 gross_billable_budget
 gross_billable_budget_loc
 line_gross_budget_booked_loc
 line_gross_budget_booked
 line_gross_rate_loc
 line_gross_rate
 created_byupdated_by
 created_at
 updated_at
 billing_cap_override
 billing_source)


(dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "territory"))


  (relate (contains-list-of IO_LINE_ITEM_MONTHLY_BUDGET  :inside-prop "monthly_budgets")
          (contains-list-of IO_LINE_ITEM_DISCOUNT        :inside-prop "discounts"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity IO_LINE_ITEM_MONTHLY_BUDGET
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 actual_cost_loc
 estimated_cost_loc
 month
 budget_booked
 budget_booked_loc
 quantity_delivered
 budget_delivered
 billable_quantity
 billable_budget_loc
 billable_budget
 3p_quantity_delivered
 3p_budget_delivered
 3p_budget_loc_delivered
 budget_delivered_loc
 forecast_budget
 forecast_budget_loc
 quantity_booked
 enable_billing_override
 gross_billable_budget
 gross_billable_budget_loc)

   (dynamic-fields(custom-fields :from "custom_fields" :prefix "custom_"))


 (relate (needs IO_LINE_ITEM : prop "id")
         (contains-list-of IO_LINE_ITEM_MONTHLY_BUDGET_DISCOUNT :inside-prop "monthly_budgets/discounts")))




(entity IO_LINE_ITEM_MONTHLY_BUDGET_DISCOUNT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 discount_amount
 percentage)

 (relate (needs IO_LINE_ITEM_MONTHLY_BUDGET : prop "id")))



(entity IO_LINE_ITEM_DISCOUNT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 percentage)

 (relate (needs IO_LINE_ITEM : prop "id")))



(entity ITEMIZED_COST
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/ios/{io_id}/line_items/{line_item_id}/itemized_costs")
  (extract-path ""))
  
(fields
 id :id
 description
 actual_cost_loc
 actual_cost
 cost_type
 )


  (relate (contains-list-of ITEMIZED_COST_MONTHLY  :inside-prop "monthlies")
          (needs IO_ITEM :prop "id")
          (needs IO_LINE_ITEM :prop "io_id")))
          

(entity ITEMIZED_COST_MONTHLY
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 index :id :index
 actual_cost_loc
 actual_cost
 month)

 (relate (needs ITEMIZED_COST : prop "id")))



(entity LEAD
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/leads")
  (extract-path ""))
  
(fields
 id :id
 email
 status
 state
 account
 contact
 company_name
 budget
 notes
 first_name
 last_name
 title
 created_at
 updated_at
 rejected_at
 rejected_reason
 accepted_at
 reassigned_at
 country
 product_name
 phone_number
 user(json))

(dynamic-fields
  (custom-fields :from "custom_fields" :prefix "custom_")

    (flatten-fields
      (fields
        name
        info
        url)
      :from "source"))


  (relate (contains-list-of LEAD_DEAL :inside-prop "deals"))
          
  (sync-plan
    (change-capture-cursor)
    
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional


(entity LEAD_DEAL
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name)

 (relate (needs LEAD : prop "id")))




(entity MEDIA_PLAN
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/media_plans")
  (extract-path ""))
  
(fields
 id :id
 deal_id
 deal_name
 owner_id
 name
 budget
 budget_loc
 budget_goal
 budget_goal_loc
 media_plan_quantity
 media_plan_ecpm
 start_date
 end_date
 margin
 status
 is_primary
 is_current
 is_locked
 rate_card_id
 rate_card_name
 cost
 cost_loc
 gam_proposal_id
 gross_budget
 gross_budget_loc
 created_by
 updated_by
 created_at
 updated_at)


  (relate (contains-list-of MEDIA_PLAN_DISCOUNT :inside-prop "discounts"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional


(entity MEDIA_PLAN_DISCOUNT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 percentage)

 (relate (needs MEDIA_PLAN : prop "id")))




(entity MEDIA_PLAN_LINE_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/media_plans/{media_plan_id}/line_items")
  (extract-path ""))
  
(fields
 id :id
 available_units
 name
 start_date
 end_date
 budget
 budget_loc
 rate
 rate_loc
 quantity
 gross_budget_loc
 gross_rate_loc
 media_plan_quantity
 media_plan_ecpm
 is_added_value
 est_cost
 est_cost_loc
 margin
 cost_adjustment
 gam_deal_type
 created_at
 updated_at
 created_by
 updated_by
 quantity_type)


(dynamic-fields
(custom-fields :from "custom_fields" :prefix "custom_")
    (flatten-fields
      (fields
        id)
      :from "deal_product")
      
      (flatten-fields
      (fields
        id)
      :from "media_plan")
      
      (flatten-fields
      (fields
        id
        name)
      :from "package")
      
      (flatten-fields
      (fields
        id
        name)
      :from "product")
      
      (flatten-fields
      (fields
        id
        name)
      :from "rate_cart")
      
      (flatten-fields
      (fields
        id
        name)
      :from "rate_type")
      
      (flatten-fields
      (fields
        id
        name)
      :from "territory"))


  (relate (contains-list-of  MEDIA_PLAN_LINE_ITEM_MONTHLY :inside-prop "line_item_monthlies")
          (contains-list-of MEDIA_PLAN_LINE_ITEM_DISCOUNT :inside-prop "discounts")
          (needs MEDIA_PLAN :prop "id"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity MEDIA_PLAN_LINE_ITEM_MONTHLY
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 index :id :index
 month
 quantity
 budget_loc
 budget
 est_cost_loc
 est_cost)

 (relate (needs MEDIA_PLAN_LINE_ITEM : prop "id")))




(entity MEDIA_PLAN_LINE_ITEM_DISCOUNT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name
 percentage)

 (relate (needs MEDIA_PLAN_LINE_ITEM : prop "id")))




 (entity MERGE_ACCOUNT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/transactions")
  (extract-path ""))
  
(fields
 id :id
 status
 user_id
 user_email
 timestamp
 retained_account
 account_type)



  (relate (contains-list-of  MERGE_ACCOUNT_DETAIL :inside-prop "merged_accounts" as merged_account)))


(entity MERGE_ACCOUNT_DETAIL
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 index :id :index
 merged_account
 )

 (relate (needs MERGE_ACCOUNT : prop "id")))




(entity PMP
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/pmps")
  (extract-path ""))
  
(fields
 id :id
 name
 deal_id
 advertiser_id
 advertiser_name
 agency_id
 agency_name
 lead_id
 budget
 budget_net
 budget_loc
 budget_net_loc
 budget_delivered
 budget_delivered_net
 budget_delivered_loc
 budget_delivered_net_loc
 budget_remaining
 budget_remaining_net
 budget_remaining_loc
 budget_remaining_net_loc
 currency
 start_date
 end_date
 updated_at)


  (relate (contains-list-of PMP_INTREGRATION :inside-prop "integrations")
          (contains-list-of PMP_MEMBER       :inside-prop "pmp_members"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity PMP_INTREGRATION
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 external_id
 external_type)

 (relate (needs PMP : prop "id")))


(entity PMP_MEMBER
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id
 email
 share
 from_date
 to_date)

 (relate (needs PMP : prop "id")))




(entity PMP_ITEM_DAILY_ACTUAL
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/pmps/{pmp_id}/pmp_items/{pmp_item_id}/pmp_item_daily_actuals")
  (extract-path ""))
  
(fields
 id :id
 data
 ad_unit
 ad_requests
 cpm
 cpm_net
 ssp_advertiser
 impressions
 win_rate
 revenue
 revenue_loc
 revenue_net
 revenue_net_loc
 created_at
 updated_at
 created_by
 updated_by)


(dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "advertiser"))


  (relate (needs PMP      :prop "id")
          (needs PMP_ITEM :prop "id"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity PMP_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/pmps/{pmp_id}/pmp_items")
  (extract-path ""))
  
(fields
 id :id
 ssp_deal_id
 pmp_type
 budget
 budget_loc
 budget_delivered
 budget_delivered_loc
 budget_remaining
 budget_remaining_loc
 budget_net_delivered
 budget_net_delivered_loc
 budget_net_remaining
 budget_net_remaining_loc
 created_at
 updated_at
 created_by
 updated_by)


(dynamic-fields
 (custom-fields :from "custom_fields" :prefix "custom_")
    (flatten-fields
      (fields
        id
        name)
      :from "ssp")
      
    (flatten-fields
      (fields
        id
        name)
      :from "product")
      
    (flatten-fields
      (fields
        id
        name)
      :from "territory"))


  (relate (needs PMP :prop "id"))

          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity PRODUCT_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/products")
  (extract-path ""))
  
(fields
 id :id
 name
 full_name
 parent_id
 top_parent_id
 active
 pg_enabled
 rate_type_id
 level
 revenue_type
 margin
 created_at
 updated_at
 product_type)


(dynamic-fields
 (custom-fields :from "custom_fields" :prefix "custom_")
    (flatten-fields
      (fields
        id
        name)
      :from "product_family"))
      
        
    (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))  



(entity QUOTA
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/quotas")
  (extract-path ""))
  
(fields
 id :id
 type
 product
 product_family
 time_period
 quote
 currency)


(dynamic-fields
    (flatten-fields
      (fields
        id
        email
        first_name
        last_name)
      :from "user"))
      
        
    (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"        
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))



(entity RATE_CARD
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/rate_cards")
  (extract-path ""))
  
(fields
 id :id
 name
 active
 description
 currency
 default
 products_count
 created_at
 updated_at
 created_by
 updated_by)
      
        
    (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"        
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))



(entity INVENTORY_ATTRIBUTE_CATAGORY
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/settings/inventory_managements/attribute_categories")
  (extract-path ""))
  
(fields
 id :id
 name
 description
 active))



(entity INVENTORY_ATTRIBUTE
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/settings/inventory_managements/attributes")
  (extract-path ""))
  
(fields
 id :id
 name
 description
 active)

 (dynamic-fields
  (flatten-fields
  (fields
  id
  name)
  :from "category")))



(entity INVENTORY_ITEM_PERIOD
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/settings/inventory_managements/items/{item_id}/available_periods")
  (extract-path ""))
  
(fields
 id :id
 start_date
 end_date
 total_available_quantity
 unsold_quantity
 max_quantity_per_deal
 distributed_booking)

 (dynamic-fields
  (flatten-fields
  (fields
  id
  name)
  :from "category"))
  
(relate (needs INVENTORY_ITEM :prop "id")))



(entity INVENTORY_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/settings/inventory_managements/items")
  (extract-path ""))
  
(fields
 id :id
 name
 description
 start_date
 end_date
 active
 color
 time_period_increment
 time_period_schedule_type)

  
(relate (needs INVENTORY_ITEM :prop "id")
        (contains-list-of INVENTORY_ITEM_ATTRIBUTE_CATAGORY :inside-prop "attribute_category")))


(entity INVENTORY_ITEM_ATTRIBUTE_CATAGORY
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 name)

 (relate (needs INVENTORY_ITEM : prop "id")))



(entity AGREEMENT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/agreements")
  (extract-path ""))
  
(fields
 id :id
 name
 start_date
 end_date
 status
 type
 auto_track
 target
 budget_fulfilled
 budget_remaining
 created_at
 updated_at
 )


(dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "agency_holding"))


  (relate (contains-list-of  AGREEMENT_DEAL :inside-prop "deals"))
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))   ;;optional



(entity AGREEMENT_DEAL
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
 id :id
 start_date
 end_date
 name
 budget)

 (dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "advertiser")
      
    (flatten-fields
      (fields
        id
        name)
      :from "agency")
      
    (flatten-fields
      (fields
        id
        name)
      :from "stage"))

 (relate (needs AGREEMENT : prop "id")))



(entity STAGE_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/stages")
  (extract-path ""))
  
(fields
 id :id
 name
 probablity
 deals
 active
 open)
          
  (sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))




(entity TEAM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/teams")
  (extract-path ""))
  

(fields
 id :id
 name
 team_type
 members_count)

 (dynamic-fields
    (flatten-fields
      (fields
        id
        name)
      :from "top_parent")
      
    (flatten-fields
      (fields
        id
        name)
      :from "parent")
      
    (flatten-fields
      (fields
        id
        name)
      :from "leader"))

(relate (contains-list-of TEAM_MEMBER    :inside-prop "members" as member)
        (contains-list-of TEAM_PRODUCT   :inside-prop "products")
        (contains-list-of TEAM_RATE_CARD :inside-prop "rate_cards"))

(sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))



(entity TEAM_MEMBER
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  index :id :index
  member)
  
 (relate (needs TEAM : prop "id")))


(entity TEAM_PRODUCT
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  id :id
  name)
  
 (relate (needs TEAM : prop "id")))


(entity TEAM_RATE_CARD
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  id :id
  name)
  
 (relate (needs TEAM : prop "id")))




(entity TRAFFIC_LINE_ITEM
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/traffic_line_items")
  (extract-path ""))

(fields
 id :id
 name
 traffic_order_id
 io_id
 io_line_item_id
 rate_type
 start_date
 end_date
 curr_cd
 rate
 rate_loc
 budget_loc
 budget
 quantity
 buffer
 buffered_quantity
 buffered_rate
 buferred_rate_loc
 exclude_reporting
 created_at
 updated_at
 ad_server_fields(json))

 (dynamic-fields
  (custom-fields :from "custom_fields" :prefix "custom_"))

(relate (contains-list-of TRAFFIC_LINE_ITEM_INTEGRATION_ID      :inside-prop "integration_id")
        (contains-list-of TRAFFIC_LINE_ITEM_INVENTORY_TARGETING_INCLUDED  :inside-prop "inventory_targeting/included" as inventory_include)
        (contains-list-of TRAFFIC_LINE_ITEM_INVENTORY_TARGETING_EXCLUDED  :inside-prop "inventory_targeting/excluded" as inventory_exclude)
        (contains-list-of TRAFFIC_LINE_ITEM_LOCATION_TARGETING_INCLUDED   :inside-prop "location_targeting/included"  as location_include)
        (contains-list-of TRAFFIC_LINE_ITEM_LOCATION_TARGETING_EXCLUDED   :inside-prop "location_targeting/excluded"  as location_exclude))

(sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"               ;;also accepts all other query templating methods
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))



(entity TRAFFIC_LINE_ITEM_INTEGRATION_ID
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  integrations_id :id
  external_text_id
  external_id
  external_type)
  
 (relate (needs TRAFFIC_LINE_ITEM : prop "id")))


 (entity TRAFFIC_LINE_ITEM_INVENTORY_TARGETING_INCLUDED
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  index :id :index
  inventory_include)
  
 (relate (needs TRAFFIC_LINE_ITEM : prop "id")))


(entity TRAFFIC_LINE_ITEM_INVENTORY_TARGETING_EXCLUDED
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  index :id :index
  inventory_exclude)
  
 (relate (needs TRAFFIC_LINE_ITEM : prop "id")))


(entity TRAFFIC_LINE_ITEM_LOCATION_TARGETING_INCLUDED
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  index :id :index
  location_include)
  
 (relate (needs TRAFFIC_LINE_ITEM : prop "id")))


(entity TRAFFIC_LINE_ITEM_LOCATION_TARGETING_EXCLUDED
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")

 (fields
  index :id :index
  location_exclude)
  
 (relate (needs TRAFFIC_LINE_ITEM : prop "id")))



(entity USER
 (api-docs-url "https://sandbox.boostr.com/api-docs/index.html")
 (source (http/get :url "/users")
  (extract-path ""))
  

(fields
 id :id
 first_name
 last_name
 email
 title
 win_rate
 average_deal_size
 type
 is_active
 employee_id
 team_id :<= "team.id")

(relate (links-to TEAM :prop "team_id"))

(sync-plan
    (change-capture-cursor)
    
    (subset/by-time
    (query-params
      "updated_at_start" "$FROM"
      "updated_at_end" "$TO")
    (format "iso-8601" :truncated-to "hr"))))



